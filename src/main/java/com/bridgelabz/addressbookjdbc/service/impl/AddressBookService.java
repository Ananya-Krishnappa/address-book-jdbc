/**
 * Purpose:Address book JDBC connection
 * @author Ananya K
 * @version 1.0
 * @since 08/07/2021
 * 
 */
package com.bridgelabz.addressbookjdbc.service.impl;

import java.util.List;

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

	/**
	 * Function to delete contact by name
	 */
	@Override
	public int deleteContactByName(String name) throws AddressBookException {
		try {
			int result = addressBookRepository.deleteContactByName(name);
			return result;
		} catch (Exception e) {
			throw new AddressBookException(e.getMessage());
		}
	}

	/**
	 * Function to add multiple contacts to address book
	 */
	@Override
	public List<AddressBook> addMultipleContactsToAddressBook(List<AddressBook> addressBookList)
			throws AddressBookException {
		try {
			List<AddressBook> newAddressBookList = addressBookRepository
					.addMultipleContactsToAddressBook(addressBookList);
			return newAddressBookList;
		} catch (Exception e) {
			throw new AddressBookException(e.getMessage());
		}
	}

	/**
	 * Function to search person by city
	 */
	@Override
	public List<AddressBook> searchPersonByCity(String city) throws AddressBookException {
		try {
			List<AddressBook> result = addressBookRepository.searchPersonByCity(city);
			return result;
		} catch (Exception e) {
			throw new AddressBookException(e.getMessage());
		}
	}
}
