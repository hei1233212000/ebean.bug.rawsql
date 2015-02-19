package com.rawsql;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.EbeanServerFactory;
import com.avaje.ebean.RawSql;
import com.avaje.ebean.RawSqlBuilder;
import com.avaje.ebean.config.DataSourceConfig;
import com.avaje.ebean.config.ServerConfig;
import com.entity.SqlUser;
import com.entity.User;
import com.google.common.collect.Lists;
import org.avaje.agentloader.AgentLoader;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RawSqlTest {
	private static EbeanServer eb;

	@BeforeClass public static void setUpBeforeClass() throws Exception {
		AgentLoader.loadAgentFromClasspath("avaje-ebeanorm-agent", "debug=1");
		ServerConfig config = new ServerConfig();
		config.setName("test");

		DataSourceConfig h2 = new DataSourceConfig();
		h2.setDriver("org.h2.Driver");
		h2.setUsername("sa");
		h2.setPassword("");
		h2.setUrl("jdbc:h2:mem:~/test");
		h2.setHeartbeatSql("select count(*) from t_one");

		config.setDataSourceConfig(h2);

		config.setDdlGenerate(true);
		config.setDdlRun(true);

		config.setDefaultServer(true);
		config.setRegister(true);

		config.addClass(User.class);
		config.addClass(SqlUser.class);

		eb = EbeanServerFactory.create(config);
	}

	@Before public void setUp() throws Exception {
		eb.save(Lists.newArrayList(new User(1L, "Peter"), new User(2L, "May")));
	}

	@Test public void test() {
		assertEquals("There should have 2 user records", 2, eb.find(User.class).findList().size());

		String sql = "select u.id, u.name from user u where u.id > :id";
		RawSql rawSql = RawSqlBuilder.unparsed(sql).columnMapping("u.id", "id").columnMapping("u.name", "name")
				.create();

		System.out.printf("1st time: %s\n", eb.find(SqlUser.class).setRawSql(rawSql).setParameter("id", 0L).findList());

		// Exception thrown in the 2 query
		System.out.printf("2st time: %s\n", eb.find(SqlUser.class).setRawSql(rawSql).setParameter("id", 0L).findList());
	}

}
