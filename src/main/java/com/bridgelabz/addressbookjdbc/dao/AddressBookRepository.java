package com.bridgelabz.addressbookjdbc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bridgelabz.addressbookjdbc.dto.AddressBook;
import com.bridgelabz.addressbookjdbc.exception.AddressBookException;
import com.bridgelabz.addressbookjdbc.exception.JdbcConnectorException;
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

	/**
	 * delete contact by name
	 * 
	 * @param name
	 * @return int
	 * @throws AddressBookException
	 */
	public int deleteContactByName(String name) throws AddressBookException {
		try (Connection connection = JdbcConnectionFactory.getJdbcConnection()) {
			String query = "delete from address_book WHERE first_name = ?";
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, name);
			int resultSet = preparedStatement.executeUpdate();
			return resultSet;
		} catch (SQLException e) {
			LOG.error("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
			throw new AddressBookException("SQL State: " + e.getSQLState() + " " + e.getMessage());
		} catch (Exception e) {
			throw new AddressBookException(e.getMessage());
		}
	}

	/**
	 * Function to add multiple contacts to address book using batch execution
	 * 
	 * @param addressBookList
	 * @return List<AddressBook>
	 * @throws AddressBookException
	 * @throws JdbcConnectorException
	 * @throws SQLException
	 */
	public List<AddressBook> addMultipleContactsToAddressBook(List<AddressBook> addressBookList)
			throws AddressBookException, JdbcConnectorException, SQLException {
		Connection connection = JdbcConnectionFactory.getJdbcConnection();
		try {
			connection.setAutoCommit(false);
			PreparedStatement pstmt = connection.prepareStatement(
					"insert into address_book(first_name,last_name,address,city,state,zip,phone_num,email) "
							+ "values(?,?,?,?,?,?,?,?)");
			for (int i = 0; i < addressBookList.size(); i++) {
				pstmt.setString(1, addressBookList.get(i).getFirstName());
				pstmt.setString(2, addressBookList.get(i).getLastName());
				pstmt.setString(3, addressBookList.get(i).getAddress());
				pstmt.setString(4, addressBookList.get(i).getCity());
				pstmt.setString(5, addressBookList.get(i).getState());
				pstmt.setString(6, addressBookList.get(i).getZip());
				pstmt.setString(7, addressBookList.get(i).getPhoneNumber());
				pstmt.setString(8, addressBookList.get(i).getEmail());
				pstmt.addBatch();
			}
			try {
				pstmt.executeBatch();
			} catch (SQLException e) {
				LOG.error("Error message: " + e.getMessage());
				throw new AddressBookException(e.getMessage());
			}
			connection.commit();
			Statement stmt = connection.createStatement();
			ResultSet rs = null;
			rs = stmt.executeQuery("SELECT * from address_book");
			List<AddressBook> newAddressBookList = mapResultSetToAddressBookList(rs);
			return newAddressBookList;
		} catch (Exception e) {
			connection.rollback();
			throw new AddressBookException(e.getMessage());
		} finally {
			connection.close();
		}
	}

	public List<AddressBook> searchPersonByCity(String city) throws AddressBookException {
		try (Connection connection = JdbcConnectionFactory.getJdbcConnection()) {
			String query = "select * from address_book WHERE city = ?";
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, city);
			ResultSet rs = preparedStatement.executeQuery();
			List<AddressBook> addressBookList = mapResultSetToAddressBookList(rs);
			return addressBookList;
		} catch (SQLException e) {
			LOG.error("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
			throw new AddressBookException("SQL State: " + e.getSQLState() + " " + e.getMessage());
		} catch (Exception e) {
			throw new AddressBookException(e.getMessage());
		}
	}

	/**
	 * Function to map result set to address book list
	 * 
	 * @param rs
	 * @return List<AddressBook>
	 * @throws SQLException
	 */
	private List<AddressBook> mapResultSetToAddressBookList(ResultSet rs) throws SQLException {
		List<AddressBook> addressBookList = new ArrayList<AddressBook>();
		while (rs.next()) {
			AddressBook addressBook = new AddressBook();
			addressBook.setId(rs.getInt("id"));
			addressBook.setAddress(rs.getString("address"));
			addressBook.setCity(rs.getString("city"));
			addressBook.setEmail(rs.getString("email"));
			addressBook.setFirstName(rs.getString("first_name"));
			addressBook.setLastName(rs.getString("last_name"));
			addressBook.setPhoneNumber(rs.getString("phone_num"));
			addressBook.setState(rs.getString("state"));
			addressBook.setZip(rs.getString("zip"));
			addressBookList.add(addressBook);
		}
		return addressBookList;
	}
}
