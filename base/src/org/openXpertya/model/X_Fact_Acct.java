/** Modelo Generado - NO CAMBIAR MANUALMENTE - Disytel */
package org.openXpertya.model;
import java.util.logging.Level;
 import java.util.*;
import java.sql.*;
import java.math.*;
import org.openXpertya.util.*;
/** Modelo Generado por Fact_Acct
 *  @author Comunidad de Desarrollo Libertya*         *Basado en Codigo Original Modificado, Revisado y Optimizado de:*         * Jorg Janke 
 *  @version  - 2014-04-09 10:35:26.081 */
public class X_Fact_Acct extends org.openXpertya.model.PO
{
/** Constructor estándar */
public X_Fact_Acct (Properties ctx, int Fact_Acct_ID, String trxName)
{
super (ctx, Fact_Acct_ID, trxName);
/** if (Fact_Acct_ID == 0)
{
setAccount_ID (0);
setAD_Table_ID (0);
setAmtAcctCr (Env.ZERO);
setAmtAcctDr (Env.ZERO);
setAmtSourceCr (Env.ZERO);
setAmtSourceDr (Env.ZERO);
setC_AcctSchema_ID (0);
setC_Currency_ID (0);
setC_Period_ID (0);
setDateAcct (new Timestamp(System.currentTimeMillis()));
setDateTrx (new Timestamp(System.currentTimeMillis()));
setFact_Acct_ID (0);
setGL_Category_ID (0);
setPostingType (null);
setRecord_ID (0);
}
 */
}
/** Load Constructor */
public X_Fact_Acct (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID */
public static final int Table_ID = M_Table.getTableID("Fact_Acct");

/** TableName=Fact_Acct */
public static final String Table_Name="Fact_Acct";

protected static KeyNamePair Model = new KeyNamePair(Table_ID,"Fact_Acct");
protected static BigDecimal AccessLevel = new BigDecimal(3);

/** Load Meta Data */
protected POInfo initPO (Properties ctx)
{
POInfo poi = POInfo.getPOInfo (ctx, Table_ID);
return poi;
}
public String toString()
{
StringBuffer sb = new StringBuffer ("X_Fact_Acct[").append(getID()).append("]");
return sb.toString();
}
public static final int A_ASSET_ID_AD_Reference_ID = MReference.getReferenceID("A_Asset");
/** Set Asset.
Asset used internally or by customers */
public void setA_Asset_ID (int A_Asset_ID)
{
if (A_Asset_ID <= 0) set_Value ("A_Asset_ID", null);
 else 
set_Value ("A_Asset_ID", new Integer(A_Asset_ID));
}
/** Get Asset.
Asset used internally or by customers */
public int getA_Asset_ID() 
{
Integer ii = (Integer)get_Value("A_Asset_ID");
if (ii == null) return 0;
return ii.intValue();
}
public static final int ACCOUNT_ID_AD_Reference_ID = MReference.getReferenceID("Account_ID (No Summary)");
/** Set Account.
Account used */
public void setAccount_ID (int Account_ID)
{
set_ValueNoCheck ("Account_ID", new Integer(Account_ID));
}
/** Get Account.
Account used */
public int getAccount_ID() 
{
Integer ii = (Integer)get_Value("Account_ID");
if (ii == null) return 0;
return ii.intValue();
}
public static final int AD_ORGTRX_ID_AD_Reference_ID = MReference.getReferenceID("AD_Org (Trx)");
/** Set Trx Organization.
Performing or initiating organization */
public void setAD_OrgTrx_ID (int AD_OrgTrx_ID)
{
if (AD_OrgTrx_ID <= 0) set_ValueNoCheck ("AD_OrgTrx_ID", null);
 else 
set_ValueNoCheck ("AD_OrgTrx_ID", new Integer(AD_OrgTrx_ID));
}
/** Get Trx Organization.
Performing or initiating organization */
public int getAD_OrgTrx_ID() 
{
Integer ii = (Integer)get_Value("AD_OrgTrx_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Table.
Table for the Fields */
public void setAD_Table_ID (int AD_Table_ID)
{
set_ValueNoCheck ("AD_Table_ID", new Integer(AD_Table_ID));
}
/** Get Table.
Table for the Fields */
public int getAD_Table_ID() 
{
Integer ii = (Integer)get_Value("AD_Table_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Accounted Credit.
Accounted Credit Amount */
public void setAmtAcctCr (BigDecimal AmtAcctCr)
{
if (AmtAcctCr == null) throw new IllegalArgumentException ("AmtAcctCr is mandatory");
set_ValueNoCheck ("AmtAcctCr", AmtAcctCr);
}
/** Get Accounted Credit.
Accounted Credit Amount */
public BigDecimal getAmtAcctCr() 
{
BigDecimal bd = (BigDecimal)get_Value("AmtAcctCr");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Accounted Debit.
Accounted Debit Amount */
public void setAmtAcctDr (BigDecimal AmtAcctDr)
{
if (AmtAcctDr == null) throw new IllegalArgumentException ("AmtAcctDr is mandatory");
set_ValueNoCheck ("AmtAcctDr", AmtAcctDr);
}
/** Get Accounted Debit.
Accounted Debit Amount */
public BigDecimal getAmtAcctDr() 
{
BigDecimal bd = (BigDecimal)get_Value("AmtAcctDr");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Source Credit.
Source Credit Amount */
public void setAmtSourceCr (BigDecimal AmtSourceCr)
{
if (AmtSourceCr == null) throw new IllegalArgumentException ("AmtSourceCr is mandatory");
set_ValueNoCheck ("AmtSourceCr", AmtSourceCr);
}
/** Get Source Credit.
Source Credit Amount */
public BigDecimal getAmtSourceCr() 
{
BigDecimal bd = (BigDecimal)get_Value("AmtSourceCr");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Source Debit.
Source Debit Amount */
public void setAmtSourceDr (BigDecimal AmtSourceDr)
{
if (AmtSourceDr == null) throw new IllegalArgumentException ("AmtSourceDr is mandatory");
set_ValueNoCheck ("AmtSourceDr", AmtSourceDr);
}
/** Get Source Debit.
Source Debit Amount */
public BigDecimal getAmtSourceDr() 
{
BigDecimal bd = (BigDecimal)get_Value("AmtSourceDr");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Accounting Schema.
Rules for accounting */
public void setC_AcctSchema_ID (int C_AcctSchema_ID)
{
set_ValueNoCheck ("C_AcctSchema_ID", new Integer(C_AcctSchema_ID));
}
/** Get Accounting Schema.
Rules for accounting */
public int getC_AcctSchema_ID() 
{
Integer ii = (Integer)get_Value("C_AcctSchema_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Activity.
Business Activity */
public void setC_Activity_ID (int C_Activity_ID)
{
if (C_Activity_ID <= 0) set_ValueNoCheck ("C_Activity_ID", null);
 else 
set_ValueNoCheck ("C_Activity_ID", new Integer(C_Activity_ID));
}
/** Get Activity.
Business Activity */
public int getC_Activity_ID() 
{
Integer ii = (Integer)get_Value("C_Activity_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Business Partner .
Identifies a Business Partner */
public void setC_BPartner_ID (int C_BPartner_ID)
{
if (C_BPartner_ID <= 0) set_ValueNoCheck ("C_BPartner_ID", null);
 else 
set_ValueNoCheck ("C_BPartner_ID", new Integer(C_BPartner_ID));
}
/** Get Business Partner .
Identifies a Business Partner */
public int getC_BPartner_ID() 
{
Integer ii = (Integer)get_Value("C_BPartner_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Campaign.
Marketing Campaign */
public void setC_Campaign_ID (int C_Campaign_ID)
{
if (C_Campaign_ID <= 0) set_ValueNoCheck ("C_Campaign_ID", null);
 else 
set_ValueNoCheck ("C_Campaign_ID", new Integer(C_Campaign_ID));
}
/** Get Campaign.
Marketing Campaign */
public int getC_Campaign_ID() 
{
Integer ii = (Integer)get_Value("C_Campaign_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Currency.
The Currency for this record */
public void setC_Currency_ID (int C_Currency_ID)
{
set_ValueNoCheck ("C_Currency_ID", new Integer(C_Currency_ID));
}
/** Get Currency.
The Currency for this record */
public int getC_Currency_ID() 
{
Integer ii = (Integer)get_Value("C_Currency_ID");
if (ii == null) return 0;
return ii.intValue();
}
public static final int C_LOCFROM_ID_AD_Reference_ID = MReference.getReferenceID("C_Location");
/** Set Location From.
Location that inventory was moved from */
public void setC_LocFrom_ID (int C_LocFrom_ID)
{
if (C_LocFrom_ID <= 0) set_ValueNoCheck ("C_LocFrom_ID", null);
 else 
set_ValueNoCheck ("C_LocFrom_ID", new Integer(C_LocFrom_ID));
}
/** Get Location From.
Location that inventory was moved from */
public int getC_LocFrom_ID() 
{
Integer ii = (Integer)get_Value("C_LocFrom_ID");
if (ii == null) return 0;
return ii.intValue();
}
public static final int C_LOCTO_ID_AD_Reference_ID = MReference.getReferenceID("C_Location");
/** Set Location To.
Location that inventory was moved to */
public void setC_LocTo_ID (int C_LocTo_ID)
{
if (C_LocTo_ID <= 0) set_ValueNoCheck ("C_LocTo_ID", null);
 else 
set_ValueNoCheck ("C_LocTo_ID", new Integer(C_LocTo_ID));
}
/** Get Location To.
Location that inventory was moved to */
public int getC_LocTo_ID() 
{
Integer ii = (Integer)get_Value("C_LocTo_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Period.
Period of the Calendar */
public void setC_Period_ID (int C_Period_ID)
{
set_ValueNoCheck ("C_Period_ID", new Integer(C_Period_ID));
}
/** Get Period.
Period of the Calendar */
public int getC_Period_ID() 
{
Integer ii = (Integer)get_Value("C_Period_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Project.
Financial Project */
public void setC_Project_ID (int C_Project_ID)
{
if (C_Project_ID <= 0) set_ValueNoCheck ("C_Project_ID", null);
 else 
set_ValueNoCheck ("C_Project_ID", new Integer(C_Project_ID));
}
/** Get Project.
Financial Project */
public int getC_Project_ID() 
{
Integer ii = (Integer)get_Value("C_Project_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Sales Region.
Sales coverage region */
public void setC_SalesRegion_ID (int C_SalesRegion_ID)
{
if (C_SalesRegion_ID <= 0) set_ValueNoCheck ("C_SalesRegion_ID", null);
 else 
set_ValueNoCheck ("C_SalesRegion_ID", new Integer(C_SalesRegion_ID));
}
/** Get Sales Region.
Sales coverage region */
public int getC_SalesRegion_ID() 
{
Integer ii = (Integer)get_Value("C_SalesRegion_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Tax.
Tax identifier */
public void setC_Tax_ID (int C_Tax_ID)
{
if (C_Tax_ID <= 0) set_ValueNoCheck ("C_Tax_ID", null);
 else 
set_ValueNoCheck ("C_Tax_ID", new Integer(C_Tax_ID));
}
/** Get Tax.
Tax identifier */
public int getC_Tax_ID() 
{
Integer ii = (Integer)get_Value("C_Tax_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set UOM.
Unit of Measure */
public void setC_UOM_ID (int C_UOM_ID)
{
if (C_UOM_ID <= 0) set_ValueNoCheck ("C_UOM_ID", null);
 else 
set_ValueNoCheck ("C_UOM_ID", new Integer(C_UOM_ID));
}
/** Get UOM.
Unit of Measure */
public int getC_UOM_ID() 
{
Integer ii = (Integer)get_Value("C_UOM_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Account Date.
Accounting Date */
public void setDateAcct (Timestamp DateAcct)
{
if (DateAcct == null) throw new IllegalArgumentException ("DateAcct is mandatory");
set_ValueNoCheck ("DateAcct", DateAcct);
}
/** Get Account Date.
Accounting Date */
public Timestamp getDateAcct() 
{
return (Timestamp)get_Value("DateAcct");
}
/** Set Transaction Date.
Transaction Date */
public void setDateTrx (Timestamp DateTrx)
{
if (DateTrx == null) throw new IllegalArgumentException ("DateTrx is mandatory");
set_ValueNoCheck ("DateTrx", DateTrx);
}
/** Get Transaction Date.
Transaction Date */
public Timestamp getDateTrx() 
{
return (Timestamp)get_Value("DateTrx");
}
/** Set Description.
Optional short description of the record */
public void setDescription (String Description)
{
if (Description != null && Description.length() > 255)
{
log.warning("Length > 255 - truncated");
Description = Description.substring(0,255);
}
set_Value ("Description", Description);
}
/** Get Description.
Optional short description of the record */
public String getDescription() 
{
return (String)get_Value("Description");
}
/** Set Accounting Fact */
public void setFact_Acct_ID (int Fact_Acct_ID)
{
set_ValueNoCheck ("Fact_Acct_ID", new Integer(Fact_Acct_ID));
}
/** Get Accounting Fact */
public int getFact_Acct_ID() 
{
Integer ii = (Integer)get_Value("Fact_Acct_ID");
if (ii == null) return 0;
return ii.intValue();
}
public KeyNamePair getKeyNamePair() 
{
return new KeyNamePair(getID(), String.valueOf(getFact_Acct_ID()));
}
/** Set Budget.
General Ledger Budget */
public void setGL_Budget_ID (int GL_Budget_ID)
{
if (GL_Budget_ID <= 0) set_ValueNoCheck ("GL_Budget_ID", null);
 else 
set_ValueNoCheck ("GL_Budget_ID", new Integer(GL_Budget_ID));
}
/** Get Budget.
General Ledger Budget */
public int getGL_Budget_ID() 
{
Integer ii = (Integer)get_Value("GL_Budget_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set GL Category.
General Ledger Category */
public void setGL_Category_ID (int GL_Category_ID)
{
set_ValueNoCheck ("GL_Category_ID", new Integer(GL_Category_ID));
}
/** Get GL Category.
General Ledger Category */
public int getGL_Category_ID() 
{
Integer ii = (Integer)get_Value("GL_Category_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set journalno */
public void setjournalno (int journalno)
{
set_Value ("journalno", new Integer(journalno));
}
/** Get journalno */
public int getjournalno() 
{
Integer ii = (Integer)get_Value("journalno");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Line ID.
Transaction line ID (internal) */
public void setLine_ID (int Line_ID)
{
if (Line_ID <= 0) set_ValueNoCheck ("Line_ID", null);
 else 
set_ValueNoCheck ("Line_ID", new Integer(Line_ID));
}
/** Get Line ID.
Transaction line ID (internal) */
public int getLine_ID() 
{
Integer ii = (Integer)get_Value("Line_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Locator.
Warehouse Locator */
public void setM_Locator_ID (int M_Locator_ID)
{
if (M_Locator_ID <= 0) set_ValueNoCheck ("M_Locator_ID", null);
 else 
set_ValueNoCheck ("M_Locator_ID", new Integer(M_Locator_ID));
}
/** Get Locator.
Warehouse Locator */
public int getM_Locator_ID() 
{
Integer ii = (Integer)get_Value("M_Locator_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Product.
Product, Service, Item */
public void setM_Product_ID (int M_Product_ID)
{
if (M_Product_ID <= 0) set_ValueNoCheck ("M_Product_ID", null);
 else 
set_ValueNoCheck ("M_Product_ID", new Integer(M_Product_ID));
}
/** Get Product.
Product, Service, Item */
public int getM_Product_ID() 
{
Integer ii = (Integer)get_Value("M_Product_ID");
if (ii == null) return 0;
return ii.intValue();
}
public static final int POSTINGTYPE_AD_Reference_ID = MReference.getReferenceID("_Posting Type");
/** Presupuestaria = B */
public static final String POSTINGTYPE_Presupuestaria = "B";
/** Pendientes = E */
public static final String POSTINGTYPE_Pendientes = "E";
/** Estadisticos = S */
public static final String POSTINGTYPE_Estadisticos = "S";
/** Actual = A */
public static final String POSTINGTYPE_Actual = "A";
/** Set PostingType.
The type of amount that this journal updated */
public void setPostingType (String PostingType)
{
if (PostingType.equals("B") || PostingType.equals("E") || PostingType.equals("S") || PostingType.equals("A"));
 else throw new IllegalArgumentException ("PostingType Invalid value - Reference = POSTINGTYPE_AD_Reference_ID - B - E - S - A");
if (PostingType == null) throw new IllegalArgumentException ("PostingType is mandatory");
if (PostingType.length() > 1)
{
log.warning("Length > 1 - truncated");
PostingType = PostingType.substring(0,1);
}
set_ValueNoCheck ("PostingType", PostingType);
}
/** Get PostingType.
The type of amount that this journal updated */
public String getPostingType() 
{
return (String)get_Value("PostingType");
}
/** Set Quantity.
Quantity */
public void setQty (BigDecimal Qty)
{
set_ValueNoCheck ("Qty", Qty);
}
/** Get Quantity.
Quantity */
public BigDecimal getQty() 
{
BigDecimal bd = (BigDecimal)get_Value("Qty");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Record ID.
Direct internal record ID */
public void setRecord_ID (int Record_ID)
{
set_ValueNoCheck ("Record_ID", new Integer(Record_ID));
}
/** Get Record ID.
Direct internal record ID */
public int getRecord_ID() 
{
Integer ii = (Integer)get_Value("Record_ID");
if (ii == null) return 0;
return ii.intValue();
}
public static final int TYPEFACTACCT_AD_Reference_ID = MReference.getReferenceID("TypeFactAcct");
/** Sales = S */
public static final String TYPEFACTACCT_Sales = "S";
/** Purchase = P */
public static final String TYPEFACTACCT_Purchase = "P";
/** Payments Receipts = PR */
public static final String TYPEFACTACCT_PaymentsReceipts = "PR";
/** Vendor Payments = VP */
public static final String TYPEFACTACCT_VendorPayments = "VP";
/** Rest = R */
public static final String TYPEFACTACCT_Rest = "R";
/** Set TypeFactAcct */
public void setTypeFactAcct (String TypeFactAcct)
{
if (TypeFactAcct == null || TypeFactAcct.equals("S") || TypeFactAcct.equals("P") || TypeFactAcct.equals("PR") || TypeFactAcct.equals("VP") || TypeFactAcct.equals("R"));
 else throw new IllegalArgumentException ("TypeFactAcct Invalid value - Reference = TYPEFACTACCT_AD_Reference_ID - S - P - PR - VP - R");
if (TypeFactAcct != null && TypeFactAcct.length() > 2)
{
log.warning("Length > 2 - truncated");
TypeFactAcct = TypeFactAcct.substring(0,2);
}
set_Value ("TypeFactAcct", TypeFactAcct);
}
/** Get TypeFactAcct */
public String getTypeFactAcct() 
{
return (String)get_Value("TypeFactAcct");
}
public static final int USER1_ID_AD_Reference_ID = MReference.getReferenceID("Account_ID - User1");
/** Set User1.
User defined element #1 */
public void setUser1_ID (int User1_ID)
{
if (User1_ID <= 0) set_ValueNoCheck ("User1_ID", null);
 else 
set_ValueNoCheck ("User1_ID", new Integer(User1_ID));
}
/** Get User1.
User defined element #1 */
public int getUser1_ID() 
{
Integer ii = (Integer)get_Value("User1_ID");
if (ii == null) return 0;
return ii.intValue();
}
public static final int USER2_ID_AD_Reference_ID = MReference.getReferenceID("Account_ID - User2");
/** Set User2.
User defined element #2 */
public void setUser2_ID (int User2_ID)
{
if (User2_ID <= 0) set_ValueNoCheck ("User2_ID", null);
 else 
set_ValueNoCheck ("User2_ID", new Integer(User2_ID));
}
/** Get User2.
User defined element #2 */
public int getUser2_ID() 
{
Integer ii = (Integer)get_Value("User2_ID");
if (ii == null) return 0;
return ii.intValue();
}
}
