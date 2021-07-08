package com.bridgelabz.addressbookjdbc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bridgelabz.addressbookjdbc.dao.AddressBookRepository;
import com.bridgelabz.addressbookjdbc.dto.AddressBook;
import com.bridgelabz.addressbookjdbc.exception.AddressBookException;
import com.bridgelabz.addressbookjdbc.exception.JdbcConnectorException;
import com.bridgelabz.addressbookjdbc.service.impl.AddressBookService;

@ExtendWith(MockitoExtension.class)
public class AddressBookServiceTest {

	@InjectMocks
	private AddressBookService mockAddressBookService;

	private AddressBookService addressBookService;

	@Mock
	AddressBookRepository mockAddressBookRepository;

	@BeforeEach
	public void initialize() {
		mockAddressBookService = new AddressBookService(mockAddressBookRepository);
		addressBookService = new AddressBookService();
	}

	@Test
	public void givenAContact_whenCalledAdd_shouldCreateContactInAddressBook() throws AddressBookException {
		AddressBook addressBook = createAddressBook("goa", "Gurgoan", "983635242", "qwe@123.gmail.com", "Rocky", "Hari",
				"Karnataka", "1267");
		AddressBook newAddressBook = new AddressBook(1, addressBook.getFirstName(), addressBook.getLastName(),
				addressBook.getAddress(), addressBook.getCity(), addressBook.getState(), addressBook.getZip(),
				addressBook.getPhoneNumber(), addressBook.getEmail());
		Mockito.when(mockAddressBookRepository.addContactToAddressBook(Mockito.any(AddressBook.class)))
				.thenReturn(newAddressBook);
		AddressBook result = mockAddressBookService.addContactToAddressBook(addressBook);
		assertTrue(result.getId() == 1);
		Mockito.verify(mockAddressBookRepository).addContactToAddressBook(Mockito.any(AddressBook.class));
	}

	@Test
	public void givenAddressBook_whenCalledDeleteByName_shouldDeleteContactInAddressBook() throws AddressBookException {
		Mockito.when(mockAddressBookRepository.deleteContactByName(Mockito.anyString())).thenReturn(1);
		int result = mockAddressBookService.deleteContactByName("leo");
		assertTrue(result == 1);
		Mockito.verify(mockAddressBookRepository).deleteContactByName(Mockito.anyString());
	}

	@Test
	public void givenMultipleContacts_whenCalledCreateContactInAddressBook_shouldDoBatchInserts()
			throws AddressBookException, JdbcConnectorException, SQLException {
		List<AddressBook> addressBookList = new ArrayList<AddressBook>();
		AddressBook address = createAddressBook("goa", "Gurgoan", "983635242", "qwe@123.gmail.com", "Rocky", "Hari",
				"Karnataka", "1267");
		AddressBook address1 = createAddressBook("uyrg", "kerala", "9836399242", "kjhg@123.gmail.com", "Warner", "Kay",
				"Kerala", "127");
		addressBookList.add(address);
		addressBookList.add(address1);

		List<AddressBook> expectedAddressBookList = new ArrayList<AddressBook>(addressBookList);
		AtomicInteger index = new AtomicInteger();
		expectedAddressBookList.stream().map(addressBook -> setIdToAddressBook(addressBook, index))
				.collect(Collectors.toList());
		Mockito.when(mockAddressBookRepository.addMultipleContactsToAddressBook(Mockito.anyList()))
				.thenReturn(expectedAddressBookList);
		List<AddressBook> actualAddressBookList = mockAddressBookService
				.addMultipleContactsToAddressBook(addressBookList);
		assertEquals(expectedAddressBookList.size(), actualAddressBookList.size());
		Mockito.verify(mockAddressBookRepository).addMultipleContactsToAddressBook(Mockito.anyList());
	}

	@Test
	public void givenAddressBook_whenSearchByCity_shouldReturnValidResultFromAddressBook() throws AddressBookException {
		List<AddressBook> result = addressBookService.searchPersonByCity("Banglore");
		assertTrue(result.size() > 0);
	}

	@Test
	public void givenAddressBook_whenSearchByNonExistentCity_shouldReturnEmptyList() throws AddressBookException {
		List<AddressBook> result = addressBookService.searchPersonByCity("koppa");
		assertTrue(result.isEmpty());
	}

	@Test
	public void givenAContact_whenCalledAdd_shouldMakeDuplicateNameCheckBeforeAdding() throws AddressBookException {
		AddressBook addressBook = createAddressBook("goa", "Gurgoan", "983635242", "qwe@123.gmail.com", "Rocky", "Hari",
				"Karnataka", "1267");
		Exception exception = assertThrows(AddressBookException.class, () -> {
			addressBookService.addContactToAddressBook(addressBook);
		});
		String expectedMessage = "Duplicate entry 'Rocky-Hari' for key 'address_book.unique_name'";
		String actualMessage = exception.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));
	}

	/**
	 * Function to set id to address book
	 * 
	 * @param addressBook
	 * @param index
	 * @return AddressBook
	 */
	private AddressBook setIdToAddressBook(AddressBook addressBook, AtomicInteger index) {
		addressBook.setId(index.getAndIncrement());
		return addressBook;
	}

	/**
	 * Function to create address book
	 * 
	 * @param address
	 * @param city
	 * @param phoneNum
	 * @param email
	 * @param firstName
	 * @param lastName
	 * @param state
	 * @param zip
	 * @return AddressBook
	 */
	private AddressBook createAddressBook(String address, String city, String phoneNum, String email, String firstName,
			String lastName, String state, String zip) {
		AddressBook addressBook = new AddressBook();
		addressBook.setAddress(address);
		addressBook.setCity(city);
		addressBook.setEmail(email);
		addressBook.setFirstName(firstName);
		addressBook.setLastName(lastName);
		addressBook.setPhoneNumber(phoneNum);
		addressBook.setState(state);
		addressBook.setZip(zip);
		return addressBook;
	}
}
