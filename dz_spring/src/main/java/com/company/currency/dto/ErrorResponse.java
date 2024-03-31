package com.company.currency.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
/*/
Consist of error information: status, title, description.
 */
public class ErrorResponse {

    private int status;
    private String title;
    private String description;
}
