package com.entity;

import javax.persistence.Entity;

import com.avaje.ebean.annotation.Sql;

@Entity @Sql public class SqlUser {
	private Long id;
	private String name;

	public SqlUser() {}

	public SqlUser(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	@Override public String toString() {
		return new StringBuilder().append("{id: ").append(id).append(", name: ").append(name).append("}").toString();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
