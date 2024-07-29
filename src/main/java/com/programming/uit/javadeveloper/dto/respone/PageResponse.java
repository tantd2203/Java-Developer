package com.programming.uit.javadeveloper.dto.respone;


import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Builder
@Getter
public class PageResponse<T>  implements Serializable {
    int pageNo;
    int pageSize;
    int totalPage;
    T items;
}
