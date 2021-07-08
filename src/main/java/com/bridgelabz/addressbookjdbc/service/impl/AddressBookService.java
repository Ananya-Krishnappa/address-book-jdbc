/**
 * Purpose:Address book JDBC connection
 * @author Ananya K
 * @version 1.0
 * @since 08/07/2021
 * 
 */
package com.bridgelabz.addressbookjdbc.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bridgelabz.addressbookjdbc.dao.AddressBookRepository;
import com.bridgelabz.addressbookjdbc.dto.AddressBook;
import com.bridgelabz.addressbookjdbc.exception.AddressBookException;
import com.bridgelabz.addressbookjdbc.service.IAddressBookService;

public class AddressBookService implements IAddressBookService {
	private static final Logger LOG = LogManager.getLogger(AddressBookService.class);
	private AddressBookRepository addressBookRepository;

	public AddressBookService() {
		addressBookRepository = AddressBookRepository.getInstance();
	}

	public AddressBookService(AddressBookRepository addressBookRepository) {
		this.addressBookRepository = addressBookRepository;
	}

	@Override
	/**
	 * Function to add contacts to address book
	 */
	public AddressBook addContactToAddressBook(AddressBook addressBook) throws AddressBookException {
		try {
			AddressBook newAddressBook = addressBookRepository.addContactToAddressBook(addressBook);
			return newAddressBook;
		} catch (Exception e) {
			throw new AddressBookException(e.getMessage());
		}
	}

	@Override
	/**
	 * Function to delete contact by name
	 */
	public int deleteContactByName(String name) throws AddressBookException {
		try {
			int result = addressBookRepository.deleteContactByName(name);
			return result;
		} catch (Exception e) {
			throw new AddressBookException(e.getMessage());
		}
	}
}
