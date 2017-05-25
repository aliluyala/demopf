package com.ydy258.ydy.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import javax.persistence.EntityResult;
import javax.persistence.FieldResult;

/**
 * 商品类目表实体类
 * 
 * @author mickeylan
 * 
 */
@Entity
@Table(name = "t_goods_category")
@SqlResultSetMapping(
		name = "rs_goods_category",
		entities = {
				@EntityResult(
						entityClass = GoodsCategory.class,
						fields = {
							@FieldResult(name="id", column="id"),
							@FieldResult(name="cateName", column="cate_name")
						}
				)
		}
)
@NamedNativeQueries({
	@NamedNativeQuery(
			name = "getCategoryByStoreId",
			query = "select * from t_goods_category where id IN (select category_id from t_third_mall_goods where store_id=:storeId group by category_id order by category_id)"			
	)
})
public class GoodsCategory implements Serializable {
	private static final long serialVersionUID = 5419355840052156102L;
	private Long id;
	private String cateName; // 类目名称

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name="cate_name", length=64, nullable=false)
	public String getCateName() {
		return cateName;
	}

	public void setCateName(String cateName) {
		this.cateName = cateName;
	}
}