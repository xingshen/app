package com.steptowin.core.db.dao;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 
 * @Description 类描述：数据库操作抽象类
 * @author zg
 * @date 创建日期 2014-7-21 上午11:17:13
 * @record 修改记录：
 * @param <T>
 */
public class DaoBase<T> {
	private Class<T> mClazz;

	public DaoBase(Class<T> clazz) {
		mClazz = clazz;
	}

	/**
	 * 
	 * @Title: refresh
	 * @Description: 数据库数据修改后，调用此方法可把数据库新数据更新到该变量对象当中
	 * @param t
	 *            接收新数据库数据的变量
	 * @return int The number of rows found in the database that correspond to
	 *         the data id. This should be 1.
	 * 
	 * @throws
	 */
	public int refresh(T t) {
		if (null != t) {
			try {
				return DaoFactory.getFactory().getDao(mClazz).refresh(t);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return -1;
	}

	/**
	 * 
	 * @Title: insert
	 * @Description: 将数据插入到数据库当中 Create a new row in the database from an
	 *               object. If the object being created uses
	 *               DatabaseField.generatedId() then the data parameter will be
	 *               modified and set with the corresponding id from the
	 *               database.
	 * @param t
	 * @return int The number of rows updated in the database. This should be 1.
	 * @throws
	 */
	public int insert(T t) {
		if (null != t) {
			try {
				return DaoFactory.getFactory().getDao(mClazz).create(t);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return -1;
	}

	public void insert(List<T> ts) {
		if (null != ts && ts.size() > 0) {
			for (T t : ts) {
				insert(t);
			}
		}
	}

	/**
	 * 
	 * @Title: delete
	 * @Description: 删除数据库当中数据，以id为准 Delete the database row corresponding to
	 *               the id from the data parameter.
	 * @param t
	 * @return int
	 * @throws
	 */
	public int delete(T t) {
		if (null != t) {
			try {
				return DaoFactory.getFactory().getDao(mClazz).delete(t);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return -1;
	}

	public void delete(List<T> ts) {
		if (null != ts) {
			for (T t : ts) {
				delete(t);
			}
		}
	}

	/**
	 * 
	 * @Title: update
	 * @Description: 将数据更新到数据库当中 Store the fields from an object to the database
	 *               row corresponding to the id from the data parameter. If you
	 *               have made changes to an object, this is how you persist
	 *               those changes to the database.
	 * @param t
	 * @return int
	 * @throws
	 */
	public int update(T t) {
		if (null != t) {
			try {
				return DaoFactory.getFactory().getDao(mClazz).update(t);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return -1;
	}

	public void update(List<T> ts) {
		if (null != ts && ts.size() > 0) {
			for (T t : ts) {
				update(t);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public List<T> query(QueryBuilder<T, Integer> qb) {
		if (null != qb) {
			try {
				return (List<T>) DaoFactory.getFactory().getDao(mClazz)
						.query((PreparedQuery<T>) qb.prepare());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public void insertOrUpdate(T t) {
		try {
			DaoFactory.getFactory().getDao(mClazz).createOrUpdate(t);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void insertOrUpdate(List<T> ts) {
		insertOrUpdate(ts, false);
	}

	public void insertOrUpdate(QueryBuilder<T, Integer> qb, List<T> ts) {
		insertOrUpdate(qb, ts, false);
	}

	public void insertOrUpdate(List<T> ts, boolean refresh) {
		if (null != ts && ts.size() > 0) {
			for (T t : ts) {
				insertOrUpdate(t);
			}
		}
	}

	/**
	 * 
	 * @Title: insertOrUpdate
	 * @Description: 先去按第一个参数为条件查询数据库里面的现有数据，如果要处理的数据在数据库当中已经存在，则作更新操作，否则作插入操作。
	 *               第三个参数表示是否将数据库的内容同步到对象当中去，因为有些情况下，对象当中不包含数据库表全部数据，比如：分享的本地id
	 * @param qb
	 * @param ts
	 * @param refresh
	 *            void
	 * @throws
	 */
	public void insertOrUpdate(QueryBuilder<T, Integer> qb, List<T> ts,
							   boolean refresh) {
		if (null == qb)
			return;
		List<T> existList = query(qb);
		if (null != existList && existList.size() > 0 && null != ts) {
			for (T t : ts) {
				if (existList.contains(t)) {
					update(t);
				} else {
					insert(t);
					if (refresh) {
						refresh(t);
					}
				}
			}
		} else {
			insert(ts);
			if (refresh) {
				for (T t : ts) {
					refresh(t);
				}
			}
		}
	}

	/**
	 * 
	 * @Title: insertOrRefresh
	 * @Description: 
	 *               先去按第一个参数为条件查询数据库里面的现有数据，如果要处理的数据在数据库当中已经存在，则作refresh操作，否则作插入操作
	 *               。
	 * @param qb
	 * @param ts
	 *            void
	 * @throws
	 */
	public void insertOrRefresh(QueryBuilder<T, Integer> qb, List<T> ts) {
		if (null == qb)
			return;
		List<T> existList = query(qb);
		if (null != existList && existList.size() > 0 && null != ts) {
			for (T t : ts) {
				if (existList.contains(t)) {
					refresh(t);
				} else {
					insert(t);// 如果没有该数据，就插入
				}
			}
		} else {
			insert(ts);
		}
	}

	/**
	 * 
	 * @Title: query
	 * @Description: 返回条件查询对应的 QueryBuilder对象
	 * @param field
	 * @param value
	 * @return List<T>
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	public QueryBuilder<T, Integer> queryQB(String field, Object value) {
		try {
			QueryBuilder<T, Integer> qb = (QueryBuilder<T, Integer>) DaoFactory
					.getFactory().getDao(mClazz).queryBuilder();
			qb.where().eq(field, value);
			return qb;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @Title: queryQB
	 * @Description: 返回多个条件查询对应的 QueryBuilder对象
	 * @param kvs
	 * @return QueryBuilder<T,Integer>
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	public QueryBuilder<T, Integer> queryQB(Map<String, Object> kvs) {
		try {
			QueryBuilder<T, Integer> qb = (QueryBuilder<T, Integer>) DaoFactory
					.getFactory().getDao(mClazz).queryBuilder();
			Set<Entry<String, Object>> set = kvs.entrySet();
			Iterator<Entry<String, Object>> iterator = set.iterator();
			Where<T, Integer> where = qb.where();
			for (int i = 0; i < set.size(); i++) {
				Entry<String, Object> entry = iterator.next();
				if (i == set.size() - 1)
					where.eq(entry.getKey(), entry.getValue());
				else
					where.eq(entry.getKey(), entry.getValue()).and();
			}
			return qb;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @Title: queryQBor
	 * @Description: 一个属性对应多个可能值
	 * @param field
	 * @param vs
	 * @return QueryBuilder<T,Integer>
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	public QueryBuilder<T, Integer> queryQBor(String field, List<String> vs) {
		try {
			QueryBuilder<T, Integer> qb = (QueryBuilder<T, Integer>) DaoFactory
					.getFactory().getDao(mClazz).queryBuilder();
			Where<T, Integer> qbWhere = qb.where();
			for (int i = 0; i < vs.size(); i++) {
				if (i == 0) {
					qbWhere.eq(field, vs.get(i));
				} else {
					qbWhere.or(qbWhere.eq(field, vs.get(i)), null);
				}
			}
			return qb;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @Title: queryAll
	 * @Description: 查询某张表下面所有数据
	 * @return List<T>
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	public List<T> queryAll() {
		QueryBuilder<T, Integer> qb = (QueryBuilder<T, Integer>) DaoFactory
				.getFactory().getDao(mClazz).queryBuilder();
		return query(qb);
	}

	@SuppressWarnings("unchecked")
	public Dao<T, Integer> getDao() {
		return (Dao<T, Integer>) DaoFactory.getFactory().getDao(mClazz);
	}

	/**
	 * 
	 * @Title: count
	 * @Description: 返回表数据条数
	 * @return long
	 * @throws
	 */
	public long count() {
		try {
			return DaoFactory.getFactory().getDao(mClazz).countOf();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

}
