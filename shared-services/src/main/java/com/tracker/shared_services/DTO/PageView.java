package com.tracker.shared_services.DTO;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageView<T> {
    private List<ExpenseResponseDTO> items;
    private Long totalElements;
    private Integer currentPageElements;
    private  Boolean hasNextPage;
    private  Integer size;

}
