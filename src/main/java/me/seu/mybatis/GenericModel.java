package me.seu.mybatis;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Version;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GenericModel extends GenericReadOnlyModel {
    private static final long serialVersionUID = 6078417080154421832L;

    protected Date updated;
    protected Date deleted;
    @Version
    protected Integer version;

//    public void setAllNull() {
//        this.created = null;
//        this.updated = null;
//        this.deleted = null;
//        this.version = null;
//    }
}

