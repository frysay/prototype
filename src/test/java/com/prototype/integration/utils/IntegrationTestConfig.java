package com.prototype.integration.utils;

import java.util.Properties;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.prototype.config.AppConfig;
import com.prototype.data.dao.BasicObjectDao;
import com.prototype.integration.dao.mock.BasicObjectDaoMock;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = IntegrationTestConfig.TestConfig.class)
@WebAppConfiguration
@Transactional
public abstract class IntegrationTestConfig {

	@Autowired
	public WebApplicationContext wac;

	public MockMvc mockMvc;

	@Before
	public void setUpMockMvc() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}

	@Configuration
	@Import(AppConfig.class)
	public static class TestConfig {

		@Profile("mock")
		@Primary
		@Bean("mainDatasource")
		public DataSource mainDatasource() throws Exception {
			EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
			EmbeddedDatabase db = builder
					.setType(EmbeddedDatabaseType.H2)
					 .addScript("init.sql")
					.build();
			return db;
		}

		@Primary
		@Bean
		public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource mainDatasource) throws Exception {
			LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
			entityManagerFactoryBean.setDataSource(mainDatasource);
			entityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
			entityManagerFactoryBean.setPackagesToScan("com.prototype.data");

			Properties jpaProperties = new Properties();

			//Configures the used database dialect. This allows Hibernate to create SQL
			//that is optimized for the used database.
			jpaProperties.put("hibernate.dialect",  "org.hibernate.dialect.H2Dialect");

			//Checks the compatibility of the data between Hibernate Entity class and the Sql table
			jpaProperties.put("hibernate.hbm2ddl.auto", "create");

			//If the value of this property is true, Hibernate writes all SQL
			//statements to the console.
			jpaProperties.put("hibernate.show_sql", false);

			//If the value of this property is true, Hibernate will format the SQL
			//that is written to the console.
			jpaProperties.put("hibernate.format_sql", false);

			//This is needed to solve the issue AIS-4403 (Slow restart of billing on beta/live)
			jpaProperties.put("hibernate.temp.use_jdbc_metadata_defaults", false);

			entityManagerFactoryBean.setJpaProperties(jpaProperties);
			return entityManagerFactoryBean;
		}
		
		@Primary
		@Bean
		public BasicObjectDao basicObjectDao(DataSource mainDatasource) {
			return new BasicObjectDaoMock();
		}
	}
}
