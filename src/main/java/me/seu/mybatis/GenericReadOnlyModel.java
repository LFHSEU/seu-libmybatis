package me.seu.mybatis;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by liangfeihu on 11/19/14.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GenericReadOnlyModel implements Serializable {
    private static final long serialVersionUID = -6240585293909400691L;
    @Id
    @GeneratedValue(generator = "JDBC")
    protected Long id;

    protected Date created;


}
