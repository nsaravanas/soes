package com.saravana.soes.io.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.log4j.Logger;

import com.saravana.soes.model.Order;
import com.saravana.soes.model.Side;

public class CSVUtil {

	private static final String STOCKID = "Stock Id";
	private static final String SIDE = "Side";
	private static final String COMPANY = "Company";
	private static final String QUANTITY = "Quantity";
	private static final String REMAININGQUANTITY = "RemainingQuantity";
	private static final String STATUS = "Status";

	private static final String NEW_LINE_SEPARATOR = "\n";
	private static final char COMMA = ',';

	private static final String[] INPUT_FILE_HEADER_MAPPING = { STOCKID, SIDE, COMPANY, QUANTITY };
	private static final String[] OUTPUT_FILE_HEADER_MAPPING = { STOCKID, SIDE, COMPANY, QUANTITY, REMAININGQUANTITY,
			STATUS };

	private static final Logger LOG = Logger.getLogger(CSVUtil.class);

	public static List<Order> readFile(InputStream inputStream) {

		List<Order> orders = new ArrayList<>();
		CSVParser csvParser = null;
		BufferedReader reader = null;

		CSVFormat csvFormat = CSVFormat.DEFAULT.withSkipHeaderRecord().withHeader(INPUT_FILE_HEADER_MAPPING)
				.withDelimiter(COMMA).withRecordSeparator(NEW_LINE_SEPARATOR);

		try {
			reader = new BufferedReader(new InputStreamReader(inputStream));
			csvParser = new CSVParser(reader, csvFormat);
			List<CSVRecord> csvRecords = csvParser.getRecords();
			for (CSVRecord r : csvRecords) {
				Order order = new Order(new Integer(r.get(STOCKID)), setSide(r.get(SIDE)), r.get(COMPANY),
						new Long(r.get(QUANTITY)));
				orders.add(order);
			}
		} catch (Exception e) {
			LOG.error("CSV file read error " + e);
		} finally {
			try {
				reader.close();
				csvParser.close();
			} catch (IOException ioe) {
				LOG.error("Error while closing resource " + ioe);
			}
		}

		return orders;
	}

	public static List<Order> readFile(String filePath) {
		ClassLoader classLoader = CSVUtil.class.getClassLoader();
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(classLoader.getResource(filePath).getFile());
			return readFile(inputStream);
		} catch (FileNotFoundException e) {
			LOG.error("File not found at path " + filePath);
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				LOG.error("Error while closing resource " + e);
			}
		}
		return Collections.emptyList();

	}

	private static Side setSide(String side) {
		if (side == null)
			throw new IllegalArgumentException("SIDE can't be null");
		else if (side.equalsIgnoreCase("BUY"))
			return Side.BUY;
		else if (side.equalsIgnoreCase("SELL"))
			return Side.SELL;
		else
			throw new IllegalArgumentException("Unknown side, only [BUY,SELL] allowed");
	}

	public static void writeFile(List<Order> list, String filePath) {

		FileWriter fileWriter = null;
		CSVPrinter csvPrinter = null;

		ClassLoader classLoader = CSVUtil.class.getClassLoader();
		CSVFormat csvFormat = CSVFormat.DEFAULT.withHeader(OUTPUT_FILE_HEADER_MAPPING).withDelimiter(COMMA);

		try {
			fileWriter = new FileWriter(classLoader.getResource(filePath).getPath());
			csvPrinter = new CSVPrinter(fileWriter, csvFormat);
			for (Order o : list) {
				csvPrinter.printRecord(o.getStockId(), o.getSide(), o.getCompany(), o.getQuantity(),
						o.getRemainingQuantity(), o.getStatus());
			}
		} catch (Exception e) {
			LOG.error("CSV file write error " + e);
		} finally {
			try {
				fileWriter.flush();
				fileWriter.close();
				csvPrinter.close();
			} catch (IOException ioe) {
				LOG.error("Error while closing resource " + ioe);
			}
		}

	}

}
