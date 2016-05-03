package com.caveofprogramming.spring.web.test.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.caveofprogramming.spring.web.dao.Offer;
import com.caveofprogramming.spring.web.dao.OffersDao;

@ActiveProfiles("dev")
@ContextConfiguration(locations = {
		"classpath:com/caveofprogramming/spring/web/config/dao-context.xml",
		"classpath:com/caveofprogramming/spring/web/config/security-context.xml",
		"classpath:com/caveofprogramming/spring/web/test/config/datasource.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class OffersDaoTests {
	
	@Autowired
	private OffersDao offersDao;
	
	@Autowired
	private DataSource dataSource;
	
	@Before
	public void init(){
		JdbcTemplate jdbc = new JdbcTemplate(dataSource);
		
		jdbc.execute("delete from offer");
		jdbc.execute("delete from users");
		jdbc.execute("delete from authorities");
	}

	@Test
	public void testOffer() {
		Offer offer = new Offer("akbar","akbar@123.com","user");
		
		assertTrue("USer creation should return true",offersDao.create(offer));
		
		List<Offer> offers = offersDao.getOffers();
		
		assertEquals("Number of users should be 1", 1, offers.size());
		
		assertEquals("Created user should be identical to retreived user", offer, offers.get(0));
	
		offer = offers.get(0);
		offer.setText("New text.");
		assertTrue("Offer update should be true.",offersDao.update(offer));
		
		offersDao.delete(offer.getId());
		List<Offer> empty = offersDao.getOffers();
		
		assertEquals("Number of offers should be 0.",0,empty.size());
	}

}
