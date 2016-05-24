package com.caveofprogramming.spring.web.dao;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component("usersDao")
public class UsersDao {

	private NamedParameterJdbcTemplate jdbc;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	public void setDataSource(DataSource jdbc) {
		this.jdbc = new NamedParameterJdbcTemplate(jdbc);
	}

	@Transactional
	public boolean create(User user) {

		//BeanPropertySqlParameterSource params = new BeanPropertySqlParameterSource(user);

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("username", user.getUsername());
		params.addValue("password", passwordEncoder.encode(user.getPassword()));
		params.addValue("email", user.getEmail());
		params.addValue("enabled", user.isEnabled());
		params.addValue("authority", user.getAuthority());
		params.addValue("name", user.getName());

		return jdbc.update(
				"insert into users (username, name, password, email, enabled, authority) values (:username, :name, :password, :email, :enabled, :authority)",
				params) == 1;
	}

	public boolean exists(String username) {
		return jdbc.queryForObject(
				"select count(*) from users where username =:username",
				new MapSqlParameterSource("username", username), Integer.class) > 0;
	}

	public List<User> getAllUsers() {
		// TODO Auto-generated method stub
		return jdbc
				.query("select * from users",
						BeanPropertyRowMapper.newInstance(User.class));
	}
}
