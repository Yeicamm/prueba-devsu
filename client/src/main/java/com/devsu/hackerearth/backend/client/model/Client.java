package com.devsu.hackerearth.backend.client.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "clients")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Client extends Person {
	private String password;
	private boolean status;

	public Client(String name, String dni, String gender, int age, String address, String phone, String password, boolean status) {
        super(name, dni, gender, age, address, phone);
        this.password = password;
        this.status = status;
    }
}
