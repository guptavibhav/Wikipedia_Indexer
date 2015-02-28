/**
 * 
 */
package edu.buffalo.cse.ir.wikiindexer.wikipedia;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author nikhillo This class implements Wikipedia markup processing. Wikipedia
 *         markup details are presented here:
 *         http://en.wikipedia.org/wiki/Help:Wiki_markup It is expected that all
 *         methods marked "todo" will be implemented by students. All methods
 *         are static as the class is not expected to maintain any state.
 */
public class WikipediaParser {
	/* TODO */
	/**
	 * Method to parse section titles or headings. Refer:
	 * http://en.wikipedia.org/wiki/Help:Wiki_markup#Sections
	 * 
	 * @param titleStr
	 *            : The string to be parsed
	 * @return The parsed string with the markup removed
	 */
	public static String parseSectionTitle(String titleStr) {

		if (titleStr == null || "".equals(titleStr))
			return titleStr;

		Pattern p = Pattern.compile("(=*)(.*?)\\1");
		Matcher m = p.matcher(titleStr);
		if (m.find()) {
			return m.group(2).trim();
		}

		return null;
	}

	/* TODO */
	/**
	 * Method to parse list items (ordered, unordered and definition lists).
	 * Refer: http://en.wikipedia.org/wiki/Help:Wiki_markup#Lists
	 * 
	 * @param itemText
	 *            : The string to be parsed
	 * @return The parsed string with markup removed
	 */
	public static String parseListItem(String itemText) {

		if (itemText == null || "".equals(itemText))
			return itemText;
		// replace unnumbered and numbered lists
		itemText = itemText.replaceAll("^([*]+|[#]+)(.*)$", "$2");

		// replace definition list
		// StringBuffer strBuff = new StringBuffer();
		Pattern listPattern = Pattern
				.compile("^;(.*:.*(\\n|\\Z)|[^:]+?\\n(:.*(\\n|\\Z)))((:.*(\\n|\\Z)))*");
		Matcher m = listPattern.matcher(itemText);
		StringBuffer tempBuff = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(tempBuff, m.group(0).replaceAll("; |: ", ""));

		}
		m.appendTail(tempBuff);
		itemText = tempBuff.toString();
		tempBuff.setLength(0);

		return itemText.trim();
	}

	/* TODO */
	/**
	 * Method to parse text formatting: bold and italics. Refer:
	 * http://en.wikipedia.org/wiki/Help:Wiki_markup#Text_formatting first point
	 * 
	 * @param text
	 *            : The text to be parsed
	 * @return The parsed text with the markup removed
	 */
	public static String parseTextFormatting(String text) {

		if (text == null || "".equals(text))
			return text;

		return text.replaceAll("('''''|'''|'')(.*?)\\1", "$2");

	}

	/* TODO */
	/**
	 * Method to parse *any* HTML style tags like: <xyz ...> </xyz> For most
	 * cases, simply removing the tags should work.
	 * 
	 * @param text
	 *            : The text to be parsed
	 * @return The parsed text with the markup removed.
	 */
	public static String parseTagFormatting(String text) {

		if (text == null || "".equals(text))
			return text;

		String tempText = text;

		while (true) {
			// text = text.replaceAll("<(.*?)>([\\S\\s]*?)</\\1>", "$2");
			text = text.replaceAll("<[\\w]+.*?/>", "");
			text = text.replaceAll("<([\\w]+).*?>([\\S\\s]*?)</\\1>", "$2");
			text = text.replaceAll(
					"&lt;([\\w]+).*?&gt;([\\S\\s]*?)&lt;/\\1&gt;", "$2");
			text = text.replaceAll("\\s{2,}", " ");
			text = text.trim();
			if (text.equals(tempText))
				break;
			else
				tempText = text;
		}

		return text;
	}

	/* TODO */
	/**
	 * Method to parse wikipedia templates. These are *any* {{xyz}} tags For
	 * most cases, simply removing the tags should work.
	 * 
	 * @param text
	 *            : The text to be parsed
	 * @return The parsed text with the markup removed
	 */
	public static String parseTemplates(String text) {

		if (text == null || "".equals(text))
			return text;
		/*
		 * text = text.replace("{{", ""); text = text.replace("}}", "");
		 */
		Pattern p = Pattern
				.compile("(?<!<nowiki>)(\\{\\{|\\}\\})(?!</nowiki>)");
		Matcher m = p.matcher(text);
		Stack a = new Stack();
		int start = -1;
		

		while (m.find()) {
			if (m.group().equals("{{")) {
				// Push Into the stack
				a.push(m.start());
			} else {
				if (!a.isEmpty()) {
					start = (Integer) a.pop();
					if (a.empty()) {
						// you have a well formed text btwn start - 1 and end .
						// That can be replaced.

						char[] temp = new char[m.start() + 1 - start + 1];
						Arrays.fill(temp, (char) (555));

					
						// if replacement text in btwn
						String leftpart = text.substring(0, start);
						String rightPart = text.substring(m.start() + 2,
								text.length());
						// if (start > 0 && m.start() < text.length()-2)
						text = leftpart + (new String(temp)) + rightPart;

					} else // ELSE IS TO HANDLE UNWELLFORMED
					{

						
						char[] temp = new char[m.start() + 1 - start + 1];
						Arrays.fill(temp, (char) (555));

						
						// if replacement text in btwn
						String leftpart = text.substring(0, start);
						String rightPart = text.substring(m.start() + 2,
								text.length());
						// if (start > 0 && m.start() < text.length()-2)
						text = leftpart + (new String(temp)) + rightPart;

					}

				} else {
					// some problem
				}
			}

		}
		
		text = text.replaceAll((char) (555) + "{4,}", "");

		return text;

		//

	}

	/* TODO */
	/**
	 * Method to parse links and URLs. Refer:
	 * http://en.wikipedia.org/wiki/Help:Wiki_markup#Links_and_URLs
	 * 
	 * @param text
	 *            : The text to be parsed
	 * @return An array containing two elements as follows - The 0th element is
	 *         the parsed text as visible to the user on the page The 1st
	 *         element is the link url
	 */
	public static String[] parseLinks(String text) {

		if (text == null || "".equals(text))
			return new String[] { "", "" };

		String linkPath = null;
		String linkText = null;
		// Get the link text

		// Pattern lnkPattern = Pattern.compile("(.*?)\\[\\[(.*?)\\]\\](.*)");
		Pattern lnkPattern = Pattern
				.compile("^([^\\[]*)\\[\\[(.*?)\\]\\](.*)$");
		String prefix, postfix;
		Matcher lnkMatcher = lnkPattern.matcher(text);
		if (lnkMatcher.find()) {
			prefix = lnkMatcher.group(1);
			postfix = lnkMatcher.group(3);
			String s1 = lnkMatcher.group(2); // gives the text between [[ and ]]

			if (s1.startsWith("<nowiki />")) {
				String temp = s1.replaceAll("<nowiki />", "");
				return new String[] { "[[" + temp + "]]", "" };
			}
			// Pattern p1 = Pattern.compile("(.*?)(\\|(.*?))?");

			// Pattern p1 = Pattern.compile("([^|]*)((\\|(.*?))*)");
			Pattern p1 = Pattern.compile("^([^|]*?)((\\|([^|]*?))*)$");

			// [^|]*((\|.*?)*\|[^|]*)?

			Matcher m1 = p1.matcher(s1);

			if (m1.find()) {
				linkText = m1.group(4);
				String secondHalf = m1.group(2);
				String s2 = m1.group(1);

				// parse linkPath

				if (s2.matches("(.*?#.*?)")) {
					// use it as it is

					// linkPath = s2;
					if (linkText == null || "".equals(linkText))
						linkText = s2;

					linkPath = "";

				} else {
					// parse further
					// (.*?:)?(.*?)(,.*|"(.*?)" )?
					// namspc?link 0 or 1 comma or (.*?)
					Pattern s3 = Pattern
							.compile("^(.*?:)?(.*?)(\\(.*?\\)|,.*)?$");
					Matcher m3 = s3.matcher(s2);
					if (m3.find()) {

						String namespace = m3.group(1) == null ? "" : m3
								.group(1);
						String ignorePart = m3.group(3) == null ? "" : m3
								.group(3);
					

						if (namespace.equals("")) {
							if (secondHalf == null || "".equals(secondHalf)) {

								linkPath = m3.group(2); // should we include
														// ignore
								// path ?

								// handle language and character link part
								if (namespace.matches("(Category|en|ru):"))
									linkPath = "";

							} else {
								linkPath = namespace + m3.group(2) + ignorePart;
							}
						} else {
							linkPath = "";
						}

						if (linkText == null || "".equals(linkText)) {
							if (secondHalf == null || "".equals(secondHalf)) {
								// are we supposed to handle
								// InterwikiMap_Shortcuts ?

								linkText = (namespace.matches("Category:") ? ""
										: namespace) + m3.group(2);// +
																	// ignorePart;
																	// not sure
																	// if we
																	// should
																	// include
																	// ignore
																	// Part or
																	// not

								linkText = linkText.replaceFirst(
										"(:)(Category:(.*))", "$2");

								if (linkText.matches("(File:|Image:).*"))
									linkText = "";
							} else {
								linkText = m3.group(2).trim();
							}
						} else {
							// No need to replace link text

						}

					} else {
						// something wrong

					}

				}

				// Replace Spaces with _
				linkPath = linkPath.replaceAll(" ", "_");

				// Check for capitalization

				if (linkPath != null && !"".equals(linkPath)) // For some text
																// [[about:]]
																// link path is
																// comming empty
					linkPath = linkPath.substring(0, 1).toUpperCase()
							+ linkPath.substring(1, linkPath.length());

				// Check for capitalization

				if (linkPath.matches("(File:|Image:).*"))
					linkPath = "";

				// add prefix / postfix if any
				linkText = prefix + linkText
						+ postfix.replaceAll("<nowiki />", "");

			} else {
				// something wrong

			}

			if (lnkMatcher.group(0).startsWith("<nowiki />"))
				return new String[] { "", "" };
			else
				return new String[] { linkText, linkPath };
		} else if (text.matches(".*?\\[.*?\\].*")) {// external links
			if (text.matches("\\[<nowiki />.*")) {
				String temp = text.replaceAll("\\[<nowiki />(.*)\\]", "$1");
				return new String[] { temp, "" };
			}

			Pattern p = Pattern.compile("\\[([^\\s]+)(\\s)?(.*)\\]");
			Matcher m = p.matcher(text);
			if (m.find()) {
				if (m.group(1) != null && !"".equals(m.group(1)))
					return new String[] { m.group(3), "" };
				else
					return new String[] { "", "" };
			} else
				return new String[] { "", "" };
		}

		return null;

	}

	public static WikipediaDocument parseLinkAndCategory(WikipediaDocument doc) 
	{
		String Text = doc.getText();

		// links with the format [[ ... ]]
		Pattern linkPattern = Pattern
				.compile("(?<!<nowiki />)\\[\\[.*?\\]\\][\\w]*?(?=(\\s|\\W|\\Z))");
		Matcher m1 = linkPattern.matcher(Text);

		StringBuffer tempBuff = new StringBuffer();
		while (m1.find()) {
			String[] link = null;
			String category = null;
			String linkRawText = m1.group(0);
			try {
				Pattern categoryPattern = Pattern
						.compile("\\[\\[Category:(.*?)\\]\\]");
				Matcher catMatcher = categoryPattern.matcher(linkRawText);

				if (catMatcher.find()) {
					// Its actually a Category and not the link
					category = catMatcher.group(1);
					// add just the linkPath
					doc.addCategory(category);

				} else {
					// Its a link . Create a link and add to link collection
					link = WikipediaParser.parseLinks(linkRawText);
					// add just the linkPath
					if (link[1] != null && !"".equals(link[1]))
						doc.addLink(link[1]);
					// System.out.println("Found a section: " +s);
				}

				String replacementText = m1.group(0);
				replacementText = replacementText.replaceAll("\\[|\\]", "");
				if (link != null) {
					if (link[0] != null && !link[0].equals(""))
						replacementText = link[0];
				} else if (category != null) {
					if (!category.equals(""))
						replacementText = category;
				} else {
					replacementText = m1.group(0);
				}

				m1.appendReplacement(tempBuff,
						Matcher.quoteReplacement(replacementText));
				// Text = Text.replace(m1.group(0), );

			} catch (Exception e) {

				System.out.println("Error in processing the linkRawText  "
						+ linkRawText);
				System.out.println("errorMessage=" + e.getMessage());
				System.out.println("DocumentId=" + doc.getId()
						+ " document Title = " + doc.getTitle());

			}

		}
		m1.appendTail(tempBuff);
		doc.setText(tempBuff.toString());
		tempBuff.setLength(0);
		/**************************External links*************************************/
			
		Text = doc.getText();

		// links with the format [... ]
		 linkPattern = Pattern
				.compile("(?<!(<nowiki />|\\[))\\[.*?\\][\\w]*?(?=(\\s|\\W|\\Z))");
		 m1 = linkPattern.matcher(Text);

		tempBuff = new StringBuffer();
		while (m1.find()) {
			String[] link = null;
			
			String linkRawText = m1.group(0);
			try {
				
					// Its a link . Create a link and add to link collection
					link = WikipediaParser.parseLinks(linkRawText);
				


				String replacementText = m1.group(0);
				replacementText = replacementText.replaceAll("\\[|\\]", "");
				if (link != null) {
					if (link[0] != null && !link[0].equals(""))
						replacementText = link[0];
				} else{
					replacementText = "";// removing entire part 
				}

				m1.appendReplacement(tempBuff,
						Matcher.quoteReplacement(replacementText));
				// Text = Text.replace(m1.group(0), );

			} catch (Exception e) {

				System.out.println("Error in processing the linkRawText  "
						+ linkRawText);
				System.out.println("errorMessage=" + e.getMessage());
				System.out.println("DocumentId=" + doc.getId()
						+ " document Title = " + doc.getTitle());

			}

		}
		m1.appendTail(tempBuff);
		doc.setText(tempBuff.toString());
		tempBuff.setLength(0);
		
		/***************************************************************/
		return doc;

	}

	public static WikipediaDocument createWikiDoc(WikipediaDocument doc) {

		String Text = doc.getText();

		// Remove Unwanted Markup
		/* Text formatting i.e. Bold and italics */

		Text = WikipediaParser.parseTextFormatting(Text);

		// parse list items
		Text = WikipediaParser.parseListItem(Text);

		// remove template tags
		Text = WikipediaParser.parseTemplates(Text);

		// create a link and Category collection and remove link markup

		doc.setText(Text);
		doc = parseLinkAndCategory(doc);
		Text = doc.getText();

		/* remove tags . i.e. HTML like formatting */
		Text = WikipediaParser.parseTagFormatting(Text);

		// Create a sections collection

		List<String[]> tempCollection = new ArrayList<String[]>();
		Pattern sectionPattern = Pattern
				.compile("(={2,6})(.*?)\\1([\\s|\\S]*?)(?=(={2,6}|\\Z))");
		Matcher m = sectionPattern.matcher(Text);
		StringBuffer buff = new StringBuffer();
		while (m.find()) {

			m.appendReplacement(buff, "");
			String s = m.group(1) + m.group(2) + m.group(1);
			// send the == section == for Parsing
			String sectionTitle = WikipediaParser.parseSectionTitle(s);
			String sectionText = m.group(3);

			// doc.addSection(sectionTitle, sectionText);
			tempCollection.add(new String[] { sectionTitle, sectionText });

			// System.out.println("Found a section: " +s);
		}
		m.appendTail(buff);
		tempCollection.add(0, new String[] { "Default", buff.toString() });
		buff.setLength(0);
		for (String[] arr : tempCollection) {
			doc.addSection(arr[0], arr[1]);
		}

		doc.setText("");
		// create map for langlinks

		return doc;
	}

}
