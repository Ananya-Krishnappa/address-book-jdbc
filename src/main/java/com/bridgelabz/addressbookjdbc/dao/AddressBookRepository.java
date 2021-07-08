package com.bridgelabz.addressbookjdbc.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bridgelabz.addressbookjdbc.dto.AddressBook;
import com.bridgelabz.addressbookjdbc.exception.AddressBookException;
import com.bridgelabz.addressbookjdbc.util.JdbcConnectionFactory;

public class AddressBookRepository {
	private static final Logger LOG = LogManager.getLogger(AddressBookRepository.class);

	private static AddressBookRepository addressBookRepository;

	private AddressBookRepository() {

	}

	public static AddressBookRepository getInstance() {
		if (addressBookRepository == null) {
			addressBookRepository = new AddressBookRepository();
		}
		return addressBookRepository;
	}

	/**
	 * Function to add contact to address book
	 * 
	 * @param addressBook
	 * @return AddressBook
	 * @throws AddressBookException
	 */
	public AddressBook addContactToAddressBook(AddressBook addressBook) throws AddressBookException {
		int addressBookId = -1;
		try (Connection connection = JdbcConnectionFactory.getJdbcConnection()) {
			String query = String.format(
					"insert into address_book(first_name,last_name,address,city,state,zip,phone_num,email) "
							+ "values('%s','%s','%s','%s','%s','%s','%s','%s')",
					addressBook.getFirstName(), addressBook.getLastName(), addressBook.getAddress(),
					addressBook.getCity(), addressBook.getState(), addressBook.getZip(), addressBook.getPhoneNumber(),
					addressBook.getEmail());
			Statement statement = connection.createStatement();
			int rowAffected = statement.executeUpdate(query, statement.RETURN_GENERATED_KEYS);
			if (rowAffected == 1) {
				ResultSet result = statement.getGeneratedKeys();
				if (result.next()) {
					addressBookId = result.getInt(1);
				}
			}
			addressBook.setId(addressBookId);
			return addressBook;
		} catch (Exception e) {
			throw new AddressBookException(e.getMessage());
		}
	}
}
