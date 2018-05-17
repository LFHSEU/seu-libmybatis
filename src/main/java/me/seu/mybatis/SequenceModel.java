package me.seu.mybatis;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Version;
import java.util.Date;

/**
 * Created by liangfeihu on 2016/10/12.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SequenceModel extends SequenceReadOnlyModel{
    protected Date updated;
    protected Date deleted;
    @Version
    protected Integer version;
}
