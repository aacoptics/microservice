package com.aacoptics.ldap.sync.entity.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author Yan Shangqi
 * @since 2021-06-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("SAP_ORG_ET_MDATA")
public class SapOrgEtMdata implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("FD_ID")
    private String fdId;

    @TableField("MANDT")
    private String mandt;

    @TableField("PERNR")
    private String pernr;

    @TableField("STAT2")
    private String stat2;

    @TableField("BUKRS")
    private String bukrs;

    @TableField("WERKS")
    private String werks;

    @TableField("PERSG")
    private String persg;

    @TableField("PERSK")
    private String persk;

    @TableField("VDSK1")
    private String vdsk1;

    @TableField("BTRTL")
    private String btrtl;

    @TableField("ABKRS")
    private String abkrs;

    @TableField("KOSTL")
    private String kostl;

    @TableField("ORGEH")
    private String orgeh;

    @TableField("PLANS")
    private String plans;

    @TableField("SACHP")
    private String sachp;

    @TableField("ZHUKOU")
    private String zhukou;

    @TableField("SYQK")
    private String syqk;

    @TableField("ZRACKY")
    private String zracky;

    @TableField("ZPCODE")
    private String zpcode;

    @TableField("JGUAN")
    private String jguan;

    @TableField("GXIND")
    private String gxind;

    @TableField("NACHN")
    private String nachn;

    @TableField("VORNA")
    private String vorna;

    @TableField("RUFNM")
    private String rufnm;

    @TableField("NAME2")
    private String name2;

    @TableField("GESCH")
    private String gesch;

    @TableField("NATIO")
    private String natio;

    @TableField("KONFE")
    private String konfe;

    @TableField("FAMST")
    private String famst;

    @TableField("ZHRFZCCX")
    private String zhrfzccx;

    @TableField("ZHRFZCLB")
    private String zhrfzclb;

    @TableField("ZHRFZCMC")
    private String zhrfzcmc;

    @TableField("ZHRFZD")
    private String zhrfzd;

    @TableField("ZHRFGZDD")
    private String zhrfgzdd;

    @TableField("ZGZQY")
    private String zgzqy;

    @TableField("ZGZLD")
    private String zgzld;

    @TableField("ZGZLC")
    private String zgzlc;

    @TableField("ZCS")
    private String zcs;

    @TableField("ZBZ")
    private String zbz;

    @TableField("ZCJBM")
    private String zcjbm;

    @TableField("ZHRFGZTS")
    private String zhrfgzts;

    @TableField("ZHRFGSZ")
    private String zhrfgsz;

    @TableField("TELEPHONE")
    private String telephone;

    @TableField("EMAIL")
    private String email;

    @TableField("MOBILE")
    private String mobile;

    @TableField("SHORTMB")
    private String shortmb;

    @TableField("WECHART")
    private String wechart;

    @TableField("ZDOMAIN")
    private String zdomain;

    @TableField("KQCARD")
    private String kqcard;

    @TableField("EIPNO")
    private String eipno;

    @TableField("EMPNO")
    private String empno;

    @TableField("DOMAINB")
    private String domainb;

    @TableField("EMAILB")
    private String emailb;

    @TableField("PRIVATE_MAIL")
    private String privateMail;

    @TableField("JJLXMB")
    private String jjlxmb;

    @TableField("ZBMJL")
    private String zbmjl;

    @TableField("ZBMGJ")
    private String zbmgj;

    @TableField("ZBMZJ")
    private String zbmzj;

    @TableField("ZGJZJ")
    private String zgjzj;

    @TableField("ZHFZC")
    private String zhfzc;

    @TableField("ZGJZC")
    private String zgjzc;

    @TableField("ZEGER")
    private String zeger;

    @TableField("ZSERG")
    private String zserg;

    @TableField("HRBP")
    private String hrbp;

    @TableField("DOC_CREATE_TIME")
    private Date docCreateTime;

    @TableField("DOC_ALTER_TIME")
    private Date docAlterTime;

    @TableField("FD_IS_MANUAL")
    private Integer fdIsManual;

    @TableField("FD_IS_EFFECTIVE")
    private Integer fdIsEffective;

    @TableField("ZEVP")
    private String zevp;

    @TableField("ZIDN1")
    private String zidn1;

    @TableField("ZIDN1S")
    private String zidn1s;

    @TableField("ZIDN2")
    private String zidn2;

    @TableField("ZIDN3")
    private String zidn3;

    @TableField("ZIDN4")
    private String zidn4;

    @TableField("ZIDN5")
    private String zidn5;

    @TableField("ZIDN6")
    private String zidn6;

    @TableField("ZIDN7")
    private String zidn7;

    @TableField("ZIDN8")
    private String zidn8;

    @TableField("ZIDN9")
    private String zidn9;

    @TableField("ZCEO")
    private String zceo;

    @TableField("LEV0_DEPT_NO")
    private String lev0DeptNo;

    @TableField("LEV0_DEPT_NAME")
    private String lev0DeptName;

    @TableField("LEV1_DEPT_NO")
    private String lev1DeptNo;

    @TableField("LEV1_DEPT_NAME")
    private String lev1DeptName;

    @TableField("LEV2_DEPT_NO")
    private String lev2DeptNo;

    @TableField("LEV2_DEPT_NAME")
    private String lev2DeptName;

    @TableField("LEV3_DEPT_NO")
    private String lev3DeptNo;

    @TableField("LEV3_DEPT_NAME")
    private String lev3DeptName;

    @TableField("LEV4_DEPT_NO")
    private String lev4DeptNo;

    @TableField("LEV4_DEPT_NAME")
    private String lev4DeptName;

    @TableField("LEV5_DEPT_NO")
    private String lev5DeptNo;

    @TableField("LEV5_DEPT_NAME")
    private String lev5DeptName;
}