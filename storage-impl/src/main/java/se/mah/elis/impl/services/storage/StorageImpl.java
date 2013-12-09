package se.mah.elis.impl.services.storage;

import se.mah.elis.services.storage.Storage;
import se.mah.elis.services.storage.data.ElisDataObject;
import se.mah.elis.services.storage.exceptions.StorageException;
import se.mah.elis.services.storage.query.Query;
import se.mah.elis.services.storage.result.ResultSet;
import se.mah.elis.services.users.AbstractUser;
import se.mah.elis.services.users.UserIdentifier;

public class StorageImpl implements Storage {

	public StorageImpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void insert(ElisDataObject data) throws StorageException {
		// TODO Auto-generated method stub

	}

	@Override
	public void insert(ElisDataObject[] data) throws StorageException {
		// TODO Auto-generated method stub

	}

	@Override
	public void insert(AbstractUser user) throws StorageException {
		// TODO Auto-generated method stub

	}

	@Override
	public void insert(AbstractUser[] user) throws StorageException {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(ElisDataObject data) throws StorageException {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(ElisDataObject[] data) throws StorageException {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(AbstractUser user) throws StorageException {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(AbstractUser[] user) throws StorageException {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(ElisDataObject data) throws StorageException {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(ElisDataObject[] data) throws StorageException {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(AbstractUser user) throws StorageException {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(AbstractUser[] user) throws StorageException {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(Query query) throws StorageException {
		// TODO Auto-generated method stub

	}

	@Override
	public ElisDataObject readData(long id) throws StorageException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractUser readUser(UserIdentifier id) throws StorageException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResultSet select(Query query) throws StorageException {
		// TODO Auto-generated method stub
		return null;
	}

}
