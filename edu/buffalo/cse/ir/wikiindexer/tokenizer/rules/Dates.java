package edu.buffalo.cse.ir.wikiindexer.tokenizer.rules;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;

import java.util.regex.Pattern;
import java.util.Date;

import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenStream;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenizerException;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.rules.TokenizerRule.RULENAMES;

@RuleClass(className = RULENAMES.DATES)
public class Dates implements TokenizerRule {

	@Override
	public void apply(TokenStream stream) throws TokenizerException {
		// TODO Auto-generated method stub

		stream.IsPretokenizationState = true;
		String str = stream.TknStreamBldr.toString();
		SimpleDateFormat toDateFormat = new SimpleDateFormat("yyyyMMdd");

		String months = "January|Febrauary|March|April|May|June|July|August|September|October|November|December";
		String days = "Sunday|Monday|Tuesday|Wednesday|Thursday|Friday|Saturday";

		StringBuffer tempBuffer = new StringBuffer();

		// HH:mm:ss UTC on Sunday, 26 December 2004

		Pattern P = Pattern
				.compile("(\\d{1,2}:)?(\\d{1,2}:\\d{1,2}) [\\w]+ on (" + days
						+ "), (\\d{1,2} (" + months + ") [1-9]\\d{3})"); // space
																			// lookahead
																			// required
																			// ?
		Matcher m = P.matcher(str);

		while (m.find()) {
			
			SimpleDateFormat dtformat = new SimpleDateFormat(
					"HH:mm:ss dd MMM yyyy");
			String dateText = ("".equals(m.group(1)) ? "00:" : m.group(1))
					+ m.group(2) + " " + m.group(4);
			SimpleDateFormat TimeFormat = new SimpleDateFormat(
					"yyyyMMdd HH:mm:ss");
			Date dt = null;
			try {

				dt = dtformat.parse(dateText);
				// if date parses successfuly then append the replacement  
				String replacementString = TimeFormat.format(dt);
				
				//str = str.replace(m.group(), replacementString);
				m.appendReplacement(tempBuffer, Matcher.quoteReplacement(replacementString));
				
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				// Date failed to parse . Append the original text 
				m.appendReplacement(tempBuffer, Matcher.quoteReplacement(m.group(0)));
			}

		}
		 m.appendTail(tempBuffer);
		 str = tempBuffer.toString().trim();
		 tempBuffer.setLength(0);
		// DD MMM YYYY format

		P = Pattern.compile("[\\d]{1,2} (" + months + ") [1-9][\\d]{3}");
		m = P.matcher(str);

		while (m.find()) {
			SimpleDateFormat dtformat = new SimpleDateFormat("DD MMM yyyy");

			Date dt = null;
			try {

				dt = dtformat.parse(m.group());
				String replacementString = toDateFormat.format(dt);
				//str = str.replace(m.group(), replacementString);
				m.appendReplacement(tempBuffer, Matcher.quoteReplacement(replacementString));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				m.appendReplacement(tempBuffer, Matcher.quoteReplacement(m.group(0)));
			}

		}
		
		 m.appendTail(tempBuffer);
		 str = tempBuffer.toString().trim();
		 tempBuffer.setLength(0);

		P = Pattern.compile("(" + months + ") [\\d]{1,2}, [1-9][\\d]{3}");
		m = P.matcher(str);

		while (m.find()) {
			SimpleDateFormat dtformat = new SimpleDateFormat("MMM dd, yyyy");

			Date dt = null;
			try {

				dt = dtformat.parse(m.group());
				String replacementString = toDateFormat.format(dt);
				//str = str.replace(m.group(), replacementString);
				m.appendReplacement(tempBuffer, Matcher.quoteReplacement(replacementString));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				m.appendReplacement(tempBuffer, Matcher.quoteReplacement(m.group(0)));
			}

		}
		
		 m.appendTail(tempBuffer);
		 str = tempBuffer.toString().trim();
		 tempBuffer.setLength(0);

		// digit AD/BC //

		P = Pattern.compile("[1-9][\\d]{0,3} (AD|BC)"); // space lookahead
														// required ?
		m = P.matcher(str);

		while (m.find()) {
			SimpleDateFormat dtformat = new SimpleDateFormat("yyyy G");
			SimpleDateFormat DateFormatwithSign = new SimpleDateFormat(
					"yyyyMMdd");
			Date dt = null;
			try {

				dt = dtformat.parse(m.group());
				String replacementString = (m.group(1).toString().equals("BC") ? "-"
						: "")
						+ DateFormatwithSign.format(dt);
				//str = str.replace(m.group(), replacementString);
				m.appendReplacement(tempBuffer, Matcher.quoteReplacement(replacementString));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				m.appendReplacement(tempBuffer, Matcher.quoteReplacement(m.group(0)));
			}

		}
		m.appendTail(tempBuffer);
		 str = tempBuffer.toString().trim();
		 tempBuffer.setLength(0);

		// yyyy //

		str = str.replaceAll("(?<=\\s)([1-9][\\d]{3})(?=[\\s])", "$10101"); // end
																			// of
																			// input
																			// lookahed
																			// required
																			// ?

		// dd:dd Am/PM

		P = Pattern
				.compile("([\\d]{1,2}:[\\d]{1,2})([\\s]*)(AM|PM|am|pm|Am|Pm)"); // space
																				// lookahead
																				// required
																				// ?
		m = P.matcher(str);

		while (m.find()) {
			SimpleDateFormat dtformat = new SimpleDateFormat("hh:mm a");
			SimpleDateFormat TimeFormat = new SimpleDateFormat("HH:mm:ss");
			Date dt = null;
			try {

				dt = dtformat.parse(m.group(1) + " " + m.group(3));
				String replacementString = TimeFormat.format(dt);
				//str = str.replace(m.group(), replacementString);
				m.appendReplacement(tempBuffer, Matcher.quoteReplacement(replacementString));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				m.appendReplacement(tempBuffer, Matcher.quoteReplacement(m.group(0)));
			}

		}
		m.appendTail(tempBuffer);
		 str = tempBuffer.toString().trim();
		 tempBuffer.setLength(0);

		P = Pattern.compile("(" + months + ") \\d{1,2}"); // space lookahead
															// required ?
		m = P.matcher(str);

		while (m.find()) {
			SimpleDateFormat dtformat = new SimpleDateFormat("MMM dd");
			SimpleDateFormat TimeFormat = new SimpleDateFormat("'1900'MMdd");
			Date dt = null;
			try {

				dt = dtformat.parse(m.group(0));
				String replacementString = TimeFormat.format(dt);
				//str = str.replace(m.group(), replacementString);
				m.appendReplacement(tempBuffer, Matcher.quoteReplacement(replacementString));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				m.appendReplacement(tempBuffer, Matcher.quoteReplacement(m.group(0)));
			}

		}

		 m.appendTail(tempBuffer);
		 str = tempBuffer.toString().trim();
		 tempBuffer.setLength(0);
		// ((yy)yy)([specialchar])(yy)

		 str = str.replaceAll("\\b(([1-9]\\d)\\d{2})([\\W&&\\S])(\\d{2})\\b",
				"$10101$3$2$40101");

		stream.TknStreamBldr.setLength(0);
		stream.TknStreamBldr.append(str);
	}

}
