package com.example.outpatient;

public class Item {
 
    private String title;
    private String description;
 
    public Item(String title, String description) {
        super();
        this.setTitle(title);
        this.setDescription(description);
    }
    // getters and setters...   

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}