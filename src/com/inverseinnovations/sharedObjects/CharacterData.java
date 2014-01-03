/* eMafiaServer - CharacterData.java
GNU GENERAL PUBLIC LICENSE V3
Copyright (C) 2013  Matthew 'Apocist' Davis */
package com.inverseinnovations.sharedObjects;

public class CharacterData implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	public int eid;//this is the database id, not the normal game state id
	public String name;
	public String hexcolor;
	public String nameDisplay;
	public String avatarUrl;
}
