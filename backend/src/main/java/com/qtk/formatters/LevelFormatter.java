/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qtk.formatters;

import com.qtk.pojo.Level;
import java.text.ParseException;
import java.util.Locale;
import org.springframework.format.Formatter;

/**
 *
 * @author ngodo
 */
public class LevelFormatter implements Formatter<Level>{

    @Override
    public String print(Level level, Locale locale) {
        return String.valueOf(level.getId());
    }

    @Override
    public Level parse(String levelId, Locale locale) throws ParseException {
        Level l = new Level();
        l.setId(Integer.valueOf(levelId));
        return l;
    }
    
}
