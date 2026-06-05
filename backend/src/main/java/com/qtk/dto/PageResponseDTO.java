/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qtk.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author ngodo
 * @param <T>
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageResponseDTO<T> {
    private List<T> data;
    private int currentPage;
    private int pageSize;
    private int totalPages;
    private long totalItems;
}
