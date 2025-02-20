package vn.edu.hcmuaf.fit.demohello.repository.criteria;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchCriteria {
    private String key;     // firstName, lastName, email,...
    private String operation;   // =, >, <,...
    private Object value;

}
