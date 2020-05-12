package com.raon.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations= "classpath:root-test-context.xml")
public class DataSourceTest {

	@Autowired DataSource dataSource;
	
	@Test
	public void connection() throws SQLException {
		Connection c = null;
		try {
			c = dataSource.getConnection();
			assertThat(c.isClosed(), is(false));
		}
		finally {
			if (c != null) c.close();
		}
	}
}
