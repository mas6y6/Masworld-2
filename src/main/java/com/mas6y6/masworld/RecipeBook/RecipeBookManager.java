package com.mas6y6.masworld.RecipeBook;

import com.mas6y6.masworld.Masworld;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RecipeBookManager {
    public Map<UUID,RecipeBook> recipebookGuis;

    public RecipeBookManager() {
        recipebookGuis = new HashMap<UUID,RecipeBook>();
    }
}
