package org.adempiere.webui.apps.form;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import org.adempiere.webui.apps.AEnv;
import org.adempiere.webui.component.Checkbox;
import org.adempiere.webui.editor.WLocatorEditor;
import org.adempiere.webui.editor.WSearchEditor;
import org.adempiere.webui.event.ValueChangeEvent;
import org.adempiere.webui.event.ValueChangeListener;
import org.compiere.plaf.CompierePLAF;
import org.openXpertya.apps.form.VComponentsFactory;
import org.openXpertya.model.MBPartner;
import org.openXpertya.model.MDocType;
import org.openXpertya.model.MInOut;
import org.openXpertya.model.MInOutLine;
import org.openXpertya.model.MInvoice;
import org.openXpertya.model.MInvoiceLine;
import org.openXpertya.model.MLocator;
import org.openXpertya.model.MLocatorLookup;
import org.openXpertya.model.MLookup;
import org.openXpertya.model.MLookupInfo;
import org.openXpertya.model.MOrderLine;
import org.openXpertya.model.MProduct;
import org.openXpertya.model.MQuery;
import org.openXpertya.model.MTab;
import org.openXpertya.model.MWarehouse;
import org.openXpertya.model.PO;
import org.openXpertya.util.CLogger;
import org.openXpertya.util.DB;
import org.openXpertya.util.DisplayType;
import org.openXpertya.util.Env;
import org.openXpertya.util.Msg;
import org.openXpertya.util.Util;
import org.zkoss.zk.ui.event.Event;

public class WCreateFromShipment extends WCreateFrom {


	/**
	 * Constructor de la clase ...
	 * 
	 * 
	 * @param mTab
	 */

	protected WCreateFromShipment(MTab mTab) {
		super(mTab);
        log.info( mTab.toString());
		p_WindowNo = mTab.getWindowNo();
		AEnv.showWindow(window);
	} // VCreateFromShipment

	/** Descripción de Campos */

	private MInvoice m_invoice = null;

	/** Remito que invoca este Crear Desde */
	private MInOut inOut = null;

	private String bpDeliveryRule = null;
    
    private MBPartner bpartner = null;
	
    /** Se permite entregar más mercadería de lo devuelto? */
    private Boolean isAllowDeliveryReturns = null;
    
    /** Tipo de Documento a crear */
    private MDocType docType;
    
	/**
	 * Descripción de Método
	 * 
	 * 
	 * @return
	 * 
	 * @throws Exception
	 */

	protected boolean dynInit() throws Exception {
		log.config("");
		initAllowDeliveryReturns();

		// Buscador de facturas
		initInvoiceLookup();

		// Buscador de pedidos asociados a factura
		initInvoiceOrderLookup();

		// load Locator

		int AD_Column_ID = 3537; // M_InOut.M_Locator_ID
		MLocatorLookup locator = new MLocatorLookup(Env.getCtx(), p_WindowNo);
		locatorField = new WLocatorEditor("M_Locator_ID", true, false, true, locator, p_WindowNo);
		setDefaultLocator();

		// Entidad Comercial
		initBPartner(false);

		bPartnerField.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent evt) {
				// Se limpia la selección de factura
				invoiceField.setValue(null);
				invoiceChanged(0);

				// Si el check allInOut esta seleccionado se muestran todos los
				// pedidos del proveedor
				// que fue seleccionado.
				if (allInOut != null && allInOut.isSelected()) {
					bPartnerField.setValue(evt.getNewValue());
					showAllOrder();
				}
			}
		});

		// Remitos de Salida:
		if (isSOTrx()) {
			// Por el momento solo se permiten crear remitos de salida a partir
			// de pedidos
			// ya que el remito de salida debe estar asociado si o si a un
			// pedido.
			invoiceLabel.setVisible(false);
			invoiceField.setVisible(false);

			// Se setea el pedido que esté configurado en el encabezado del
			// remito
			if (getInOut().getC_Order_ID() > 0) {
				orderField.setValue(getInOut().getC_Order_ID());
				orderChanged(getInOut().getC_Order_ID());
			}

			// Si el remito ya contiene líneas entonces no puede editarse el
			// pedido
			// debido a que las líneas del remito están si o si asociadas con
			// las
			// líneas del pedido previamente asociado.
			if (getInOut().getLines().length > 0) {
				orderField.setReadWrite(false);
				invoiceOrderField.setReadWrite(false);
				bPartnerField.setReadWrite(false);
			}
		}

		return true;
	} // dynInit

	private void setDefaultLocator() {
		Integer warehouseId = (Integer) p_mTab.getValue("M_Warehouse_ID");
		if (warehouseId != null) {
			MWarehouse warehouse = new MWarehouse(Env.getCtx(),
					warehouseId.intValue(), null);
			MLocator locator = warehouse.getDefaultLocator();
			if (locator != null) {
				locatorField.setValue(new Integer(locator.getM_Locator_ID()));
			}
		}

	}

	/**
	 * Descripción de Método
	 * 
	 * 
	 * @param C_BPartner_ID
	 */

	protected void initBPDetails(int C_BPartner_ID) {
		log.config("C_BPartner_ID=" + C_BPartner_ID);
		updateBPDetails(C_BPartner_ID, true);
	} // initBPDetails

	@Override
    protected void updateBPDetails(Integer bpartnerID, boolean resetDocument){
    	 String deliveryRule = null;
         if(!Util.isEmpty(bpartnerID,true)){
         	setBpartner(new MBPartner(getCtx(), bpartnerID, getTrxName()));
         	deliveryRule = getBpartner().getDeliveryRule();
         }
         else{
        	 setBpartner(null);
         }
         setBpDeliveryRule(deliveryRule);
         
 		// Si la regla de envío de mercadería de la entidad comercial es Después
 		// de la Facturación entonces se debe ocultar el campo para crear remito
 		// a partir del pedido
		if (getBpDeliveryRule() != null
				&& (getBpDeliveryRule().equals(
						MBPartner.DELIVERYRULE_AfterInvoicing) || getBpDeliveryRule()
						.equals(MBPartner.DELIVERYRULE_Force_AfterInvoicing))) {
 			orderLabel.setVisible(false);
 			orderField.setVisible(false);
 			if(resetDocument){
 				// Se borra la selección de pedido.
 				orderField.setValue(null);
 				orderChanged(0);
 	 			// Se limpia la selección de factura
 				invoiceField.setValue(null);
 				invoiceChanged(0); 				
 	 		}	
         }
 		else{
 			orderLabel.setVisible(true);
 			orderField.setVisible(true);
 		}
    }
	
	/**
	 * Se inicializa el valor que determina si se debe entregar más mercadería
	 * que la devuelta
	 */
	protected void initAllowDeliveryReturns(){
		MInOut inout = getInOut();
		setDocType(MDocType.get(getCtx(), inout.getC_DocType_ID(), getTrxName()));
		setIsAllowDeliveryReturns(getDocType().isAllowDeliveryReturned());
	}
	
	/**
	 * Descripción de Método
	 * 
	 * 
	 * @param C_Invoice_ID
	 */

	private void loadInvoice(int C_Invoice_ID) {
		log.config("C_Invoice_ID=" + C_Invoice_ID);
		
		initDataTable();
		
		if (C_Invoice_ID > 0) {
			m_invoice = new MInvoice(Env.getCtx(), C_Invoice_ID, null); // save
		}
		else{
    		m_invoice = null;
    	}
		
		p_order = null;

		List<InvoiceLine> data = new ArrayList<InvoiceLine>();

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ")
				// Entered UOM
				.append("l.C_InvoiceLine_ID, ")
				.append("l.Line, ")
				.append("l.Description, ")
				.append("l.M_Product_ID, ")
				.append("p.Name AS ProductName, ")
				.append("p.value AS ItemCode, ")
				.append("p.producttype AS ProductType, ")
				.append("l.C_UOM_ID, ")
				.append("QtyInvoiced, ")
				.append("l.QtyInvoiced-SUM(NVL(mi.Qty,0)) AS RemainingQty, ")
				.append("l.QtyEntered/l.QtyInvoiced AS Multiplier, ")
				.append("COALESCE(l.C_OrderLine_ID,0) AS C_OrderLine_ID, ")
				.append("l.M_AttributeSetInstance_ID AS AttributeSetInstance_ID ")

				.append("FROM C_UOM uom, C_InvoiceLine l, M_Product p, M_MatchInv mi ")

				.append("WHERE l.C_UOM_ID=uom.C_UOM_ID ")
				.append("AND l.M_Product_ID=p.M_Product_ID ")
				.append("AND l.C_InvoiceLine_ID=mi.C_InvoiceLine_ID(+) ")
				.append("AND l.C_Invoice_ID=? ")
				.append("GROUP BY l.QtyInvoiced, l.QtyEntered/l.QtyInvoiced, l.C_UOM_ID, l.M_Product_ID, p.Name, l.C_InvoiceLine_ID, l.Line, l.C_OrderLine_ID, l.Description, p.value,l.M_AttributeSetInstance_ID,p.producttype ")
				.append("ORDER BY l.Line ");

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = DB.prepareStatement(sql.toString());
			pstmt.setInt(1, C_Invoice_ID);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				InvoiceLine invoiceLine = new InvoiceLine();

				// Por defecto no está seleccionada para ser procesada
				invoiceLine.selected = false;

				// ID de la línea de factura
				invoiceLine.invoiceLineID = rs.getInt("C_InvoiceLine_ID");

				// Nro de línea
				invoiceLine.lineNo = rs.getInt("Line");

				// Descripción
				invoiceLine.description = rs.getString("Description");

				// Cantidades
				BigDecimal multiplier = rs.getBigDecimal("Multiplier");
				BigDecimal qtyInvoiced = rs.getBigDecimal("QtyInvoiced")
						.multiply(multiplier);
				BigDecimal remainingQty = rs.getBigDecimal("RemainingQty")
						.multiply(multiplier);
				invoiceLine.lineQty = qtyInvoiced;
				invoiceLine.remainingQty = remainingQty;

				// Artículo
				invoiceLine.productID = rs.getInt("M_Product_ID");
				invoiceLine.productName = rs.getString("ProductName");
				invoiceLine.itemCode = rs.getString("ItemCode");
				invoiceLine.instanceName = getInstanceName(rs
						.getInt("AttributeSetInstance_ID"));
				invoiceLine.productType = rs.getString("ProductType");

				// Unidad de Medida
				invoiceLine.uomID = rs.getInt("C_UOM_ID");
				invoiceLine.uomName = getUOMName(invoiceLine.uomID);

				// Línea de pedido (puede ser 0)
				invoiceLine.orderLineID = rs.getInt("C_OrderLine_ID");

				// Agrega la línea a la lista solo si tiene cantidad pendiente
				if (invoiceLine.remainingQty.compareTo(BigDecimal.ZERO) > 0 || invoiceLine.productType.equals("E")) {
					data.add(invoiceLine);
				}
			}

		} catch (SQLException e) {
			log.log(Level.SEVERE, sql.toString(), e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
			} catch (Exception e) {
			}
		}
		
		filtrarColumnaInstanceName(data);
		
		loadTable(data);
		// Se carga la EC de la factura.
		if (bPartnerField != null && m_invoice != null) {
			if(bPartnerField.getValue() == null 
					|| (Integer)bPartnerField.getValue() != m_invoice.getC_BPartner_ID()){
				bPartnerField.setValue(m_invoice.getC_BPartner_ID());
				updateBPDetails(m_invoice.getC_BPartner_ID(), true);
			}
		}
	} // loadInvoice

	
	@Override
	public String getRemainingQtySQLLine(boolean forInvoice, boolean allowDeliveryReturns){
		boolean afterInvoicing = (getInOut().getDeliveryRule().equals(
				MInOut.DELIVERYRULE_AfterInvoicing) || getInOut().getDeliveryRule()
				.equals(MInOut.DELIVERYRULE_Force_AfterInvoicing))
				&& getInOut().getMovementType().endsWith("-");
		String srcColumn = afterInvoicing ? "l.QtyInvoiced" : "l.QtyOrdered";
		return srcColumn
				+ " - (l.QtyDelivered+l.QtyTransferred)"
				+ (allowDeliveryReturns ? ""
						: " - coalesce((select sum(iol.movementqty) as qty from c_orderline as ol inner join m_inoutline as iol on iol.c_orderline_id = ol.c_orderline_id inner join m_inout as io on io.m_inout_id = iol.m_inout_id inner join c_doctype as dt on dt.c_doctype_id = io.c_doctype_id where ol.c_orderline_id = l.c_orderline_id AND dt.doctypekey = 'DC' and io.docstatus IN ('CL','CO')),0)");
	}
	

	@Override
	protected boolean allowDeliveryReturned(){
    	if(isAllowDeliveryReturns == null){
    		initAllowDeliveryReturns();
    	}
    	return isAllowDeliveryReturns;
    }
	
	/**
	 * Descripción de Método
	 * 
	 * 
	 * @return
	 */

	protected void save() throws CreateFromSaveException {

		// La ubicación es obligatoria
		Integer locatorID = (Integer) locatorField.getValue();
		if (locatorID == null || (locatorID == 0)) {
			locatorField.setBackground(CompierePLAF.getFieldBackground_Error());
			throw new CreateFromSaveException("@NoLocator@");
		}

		// Actualiza el encabezado del remito (necesario para validaciones en
		// las
		// líneas a crear del remito)
		MInOut inout = getInOut();
		log.config(inout + ", C_Locator_ID=" + locatorID);
		// Asocia el pedido
		if (p_order != null) {
			inout.setC_Order_ID(p_order.getC_Order_ID());
			inout.setDateOrdered(p_order.getDateOrdered());
			inout.setC_Project_ID(p_order.getC_Project_ID());
		}
		// Asocia la factura
		if ((m_invoice != null) && (m_invoice.getC_Invoice_ID() != 0)) {
			inout.setC_Invoice_ID(m_invoice.getC_Invoice_ID());
		}

		// Guarda el encabezado. Si hay error cancela la operación
		if (!inout.save()) {
			throw new CreateFromSaveException(CLogger.retrieveErrorAsString());
		}

		// Lines
		Integer productLocatorID = null;
		MLocator productLocator = null;
		for (SourceEntity sourceEntity : getSelectedSourceEntities()) {
			DocumentLine docLine = (DocumentLine) sourceEntity;
			BigDecimal movementQty = docLine.remainingQty;
			int C_UOM_ID = docLine.uomID;
			int M_Product_ID = docLine.productID;
			
			// Determinar la ubicación relacionada al artículo y verificar que
			// se encuentre dentro del almacén del remito. Si se encuentra en
			// este almacén, entonces setearle la ubicación del artículo, sino
			// la ubicación por defecto. Sólo para movimientos de ventas.
			productLocatorID = null;
			if(isSOTrx()){
				// Obtengo el id de la ubicación del artículo
				productLocatorID = MProduct.getLocatorID(M_Product_ID, getTrxName());
				// Si posee una configurada, verifico que sea del mismo almacén,
				// sino seteo a null el id de la ubicación para que setee el que
				// viene por defecto
				if(!Util.isEmpty(productLocatorID, true)){
					productLocator = MLocator.get(getCtx(), productLocatorID);
					productLocatorID = productLocator.getM_Warehouse_ID() != inout
							.getM_Warehouse_ID() ? null : productLocatorID;
				}
			}
			
			// Crea la línea del remito
			
			MInOutLine iol = new MInOutLine(inout);
			iol.setM_Product_ID(M_Product_ID, C_UOM_ID); // Line UOM
			iol.setQty(movementQty); // Movement/Entered
			iol.setM_Locator_ID(Util.isEmpty(productLocatorID, true) ? locatorID
					: productLocatorID); // Locator
			iol.setDescription(docLine.description);

			MInvoiceLine il = null;
			MOrderLine ol = null;

			// La línea del remito se crea a partir de una línea de pedido
			if (docLine.isOrderLine()) {
				OrderLine orderLine = (OrderLine) docLine;
				// Asocia línea remito -> línea pedido
				iol.setC_OrderLine_ID(orderLine.orderLineID);
				ol = new MOrderLine(Env.getCtx(), orderLine.orderLineID,
						getTrxName());
				// Proyecto
				iol.setC_Project_ID(ol.getC_Project_ID());
				if (ol.getQtyEntered().compareTo(ol.getQtyOrdered()) != 0) {
					iol.setMovementQty(movementQty.multiply(ol.getQtyOrdered())
							.divide(ol.getQtyEntered(),
									BigDecimal.ROUND_HALF_UP));
					iol.setC_UOM_ID(ol.getC_UOM_ID());
				}
				// Instancia de atributo
				if (ol.getM_AttributeSetInstance_ID() != 0) {
					iol.setM_AttributeSetInstance_ID(ol
							.getM_AttributeSetInstance_ID());
				}
				// Cargo (si no existe el artículo)
				if (M_Product_ID == 0 && ol.getC_Charge_ID() != 0) {
					iol.setC_Charge_ID(ol.getC_Charge_ID());
				}
				
				// Este metodo es redefinido por un plugin
				customMethod(ol,iol);

				// La línea del remito se crea a partir de una línea de factura
			} else if (docLine.isInvoiceLine()) {
				InvoiceLine invoiceLine = (InvoiceLine) docLine;
				// Credit Memo - negative Qty
				if (m_invoice != null && m_invoice.isCreditMemo()) {
					movementQty = movementQty.negate();
				}
				il = new MInvoiceLine(Env.getCtx(), invoiceLine.invoiceLineID,
						getTrxName());
				// Proyecto
				iol.setC_Project_ID(il.getC_Project_ID());
				if (il.getQtyEntered().compareTo(il.getQtyInvoiced()) != 0) {
					iol.setQtyEntered(movementQty.multiply(il.getQtyInvoiced())
							.divide(il.getQtyEntered(),
									BigDecimal.ROUND_HALF_UP));
					iol.setC_UOM_ID(il.getC_UOM_ID());
				}
				// Instancia de atributo
				if (il.getM_AttributeSetInstance_ID() != 0) {
					iol.setM_AttributeSetInstance_ID(il
							.getM_AttributeSetInstance_ID());
				}
				// Cargo (si no existe el artículo)
				if (M_Product_ID == 0 && il.getC_Charge_ID() != 0) {
					iol.setC_Charge_ID(il.getC_Charge_ID());
				}
				// Si la línea de factura estaba relacionada con una línea de
				// pedido
				// entonces se hace la asociación a la línea del remito. Esto es
				// necesario
				// para que se actualicen los valores QtyOrdered y QtyReserved
				// en el Storage
				// a la hora de completar el remito.
				if (invoiceLine.orderLineID > 0) {
					iol.setC_OrderLine_ID(invoiceLine.orderLineID);
				}
				iol.setC_InvoiceLine_ID(invoiceLine.invoiceLineID);
			}
			// Guarda la línea de remito
			if (!iol.save()) {
				throw new CreateFromSaveException("@InOutLineSaveError@ (# "
						+ docLine.lineNo + "):<br>"
						+ CLogger.retrieveErrorAsString());

				// Create Invoice Line Link
			} else if (il != null) {
				il.setM_InOutLine_ID(iol.getM_InOutLine_ID());
				if (!il.save()) {
					throw new CreateFromSaveException(
							"@InvoiceLineSaveError@ (# " + il.getLine()
									+ "):<br>"
									+ CLogger.retrieveErrorAsString());
				}
			}
		} // for all rows
	} // save

	@Override
	protected String getOrderFilter() {
		return MInOut.getOrderFilter(getInOut());
	}

	@Override
	protected void orderChanged(int orderID) {
		// set Invoice and Shipment to Null
		invoiceField.setValue(null);
		invoiceOrderField.setValue(null);
		loadOrder(orderID, false, allowDeliveryReturned(), true);
		m_invoice = null;
	}

	/**
	 * Este método es invocado cuando el usuario cambia la factura seleccionada
	 * en el VLookup. Las subclases puede sobrescribir este comportamiento.
	 * 
	 * @param invoiceID
	 *            ID de la nueva factura seleccionada.
	 */
	protected void invoiceChanged(int invoiceID) {
		orderField.setValue(null);
		invoiceOrderField.setValue(null);
		loadInvoice(invoiceID);
	}

	/**
	 * Este método es invocado cuando el usuario cambia la factura con pedido
	 * seleccionada en el VLookup. Las subclases puede sobrescribir este
	 * comportamiento.
	 * 
	 * @param invoiceID
	 *            ID de la nueva factura seleccionada.
	 */
	protected void invoiceOrderChanged(int invoiceID) {
		int relatedOrderID = 0;
		orderField.setValue(null);
		invoiceField.setValue(null);
		if (invoiceID > 0) {
			MInvoice invoice = new MInvoice(getCtx(), invoiceID, getTrxName());
			relatedOrderID = invoice.getC_Order_ID();
		}
		loadOrder(relatedOrderID, false, allowDeliveryReturned(), true);
		if (relatedOrderID > 0) {
			orderField.setValue(relatedOrderID);
		}
	}

	/**
	 * @return Devuelve el remito origen de este CreateFrom.
	 */
	public MInOut getInOut() {
		if (inOut == null) {
			inOut = new MInOut(getCtx(),
					(Integer) p_mTab.getValue("M_InOut_ID"), getTrxName());
		}
		inOut.set_TrxName(getTrxName());
		return inOut;
	}

	@Override
	protected boolean beforeAddOrderLine(OrderLine orderLine) {
		// Si la línea de pedido ya está asociada con alguna línea del remito
		// entonces
		// no debe ser mostrada en la grilla. No se permite que dos líneas de un
		// mismo
		// remito compartan una línea del pedido.
		String sql = "SELECT COUNT(*) FROM M_InOutLine WHERE M_InOut_ID = ? AND C_OrderLine_ID = ?";
		Long count = (Long) DB.getSQLObject(null, sql, new Object[] {
				getInOut().getM_InOut_ID(), orderLine.orderLineID });
		if (count != null && count > 0) {
			return false;
		}

		// Para devoluciones de clientes, la cantidad pendiente es en realidad
		// la cantidad que se le ha entregado.
		if ((isSOTrx() && getInOut().getMovementType().endsWith("+"))
				|| (!isSOTrx() && getInOut().getMovementType().endsWith("-"))) {
			orderLine.remainingQty = orderLine.qtyDelivered;
		}

		return true;
	}

	/**
	 * Inicializa el lookup de facturas
	 */
	private void initInvoiceLookup() {
		String whereClause = getInvoiceFilter();
//		invoiceField = VComponentsFactory.VLookupFactory("C_Invoice_ID", "C_Invoice", p_WindowNo, DisplayType.Search, whereClause, false);
		/* FEDE:TODO: ESTO DEBERIA REFACTORIZARSE A OTRO LUGAR */
		int colID = 3484; 	// C_Invoice.C_Invoice_ID
    	MLookupInfo info = VComponentsFactory.MLookupInfoFactory( Env.getCtx(),p_WindowNo,p_mTab.getTabNo(),colID,DisplayType.Search, whereClause );
    	info.ZoomQuery = new MQuery();
    	MLookup lookup       = new MLookup(info, p_mTab.getTabNo());
    	if (whereClause != null && whereClause.length() > 0)
        	info.ZoomQuery.addRestriction(whereClause);
    	/* */
    	
    	invoiceField = new WSearchEditor("C_Invoice_ID", true, false, true, lookup);
		invoiceField.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent e)  {
				Integer invoiceID = (Integer) e.getNewValue();
				invoiceChanged(invoiceID == null ? 0 : invoiceID);
			}
		});
	}

	/**
	 * @return Devuelve el filtro que se aplica al Lookup de Facturas.
	 */
	protected String getInvoiceFilter() {
		return MInOut.getInvoiceFilter(getInOut());
	}

	/**
	 * Inicializa el lookup de facturas con pedidos asociados.
	 */
	private void initInvoiceOrderLookup() {
		String whereClause = getInvoiceOrderFilter();
//		invoiceOrderField = VComponentsFactory.VLookupFactory("C_Invoice_ID", "C_Invoice", p_WindowNo,	DisplayType.Search, whereClause, false);
		/* FEDE:TODO: ESTO DEBERIA REFACTORIZARSE A OTRO LUGAR */
		int colID = 3484; 	// C_Invoice.C_Invoice_ID
    	MLookupInfo info = VComponentsFactory.MLookupInfoFactory( Env.getCtx(),p_WindowNo,p_mTab.getTabNo(),colID,DisplayType.Search, whereClause );
    	info.ZoomQuery = new MQuery();
    	MLookup lookup = new MLookup(info, p_mTab.getTabNo());
    	if (whereClause != null && whereClause.length() > 0)
        	info.ZoomQuery.addRestriction(whereClause);
    	/* */
    	
    	invoiceOrderField = new WSearchEditor("C_Invoice_ID", true, false, true, lookup);
		invoiceOrderField.addValueChangeListener(new ValueChangeListener() {

					@Override
					public void valueChange(ValueChangeEvent e)  {
						Integer invoiceID = (Integer) e.getNewValue();
						invoiceOrderChanged(invoiceID == null ? 0 : invoiceID);
					}
				});
	}

	/**
	 * @return Devuelve el filtro que se aplica al Lookup de Facturas asociadas
	 *         a pedidos.
	 */
	protected String getInvoiceOrderFilter() {
		return MInOut.getInvoiceOrderFilter(getInOut());
	}

	public void setBpDeliveryRule(String bpDeliveryRule) {
		// TODO Sólo para ventas y movimiento de salida es necesario verificar
		// la regla de envío y el comportamiento relacionado sobre los
		// componentes e información. Cuando para proveedores también deba
		// seguir el mismo comportamiento, eliminar la condición del if de issotrx
		if (isSOTrx() && getInOut().getMovementType().endsWith("-")) {
			this.bpDeliveryRule = bpDeliveryRule;
		}
	}

	public String getBpDeliveryRule() {
		return bpDeliveryRule;
	}

	protected void setBpartner(MBPartner bpartner) {
		this.bpartner = bpartner;
	}

	protected MBPartner getBpartner() {
		return bpartner;
	}
	
	/**
	 * Entidad Orígen: Línea de Factura
	 */
	protected class InvoiceLine extends DocumentLine {
		/** ID de la línea de factura */
		protected int invoiceLineID = 0;
		/**
		 * La línea de factura puede tener asociada a su vez una línea de pedido
		 */
		protected int orderLineID = 0;

		// /** Nombre de la instancia */
		// protected String instanceName = null;

		@Override
		public boolean isInvoiceLine() {
			return true;
		}
	}

	// El método agrega el check al panel parameterStdPanel
	@Override
	protected void customizarPanel() {
		// Si es Perfil Compras agrego el check Ver todos los pedidos
		if (!isSOTrx()) {
			allInOut = new Checkbox();
			allInOut.setText("Ver todos los pedidos");
			allInOut.addActionListener(this);
			window.getParameterPanel().appendChild(allInOut);
		}

	}

	
	public void onEvent(Event e) throws Exception {
		log.config("Action=" + e.getName());

		// Si selecciona el check "Seleccionar
		if (e.getTarget().equals(allInOut))
			showAllOrder();
		else
			super.onEvent(e);

	} // actionPerformed

	// El siguiente método limpia los campos (invoiceField, orderField,
	// invoiceField) y los desactiva cuando
	// se selecciona el check "Seleccionar todos los pedidos".
	public void activeDesactiveField(boolean state) {
		if (!state) {
			invoiceField.setValue("");
			orderField.setValue("");
			invoiceOrderField.setValue("");
		}
		orderField.setReadWrite(state);
		invoiceOrderField.setReadWrite(state);
		invoiceField.setReadWrite(state);
	}

	// El método muestra todos los pedidos del proveedor seleccionado
	public void showAllOrder() {
		activeDesactiveField(!allInOut.isSelected());
		if (allInOut.isSelected()) {
			StringBuffer sql;

			// La consulta obtiene los pedidos, del proveedor seleccionado
			// aplicando el filtro getOrderFilter.
			sql = new StringBuffer();
			sql.append(
					"select * from C_Order where C_BPartner_ID = "
							+ bPartnerField.getValue() + " AND ").append(
					getOrderFilter());

			log.finer(sql.toString());

			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				pstmt = DB.prepareStatement(sql.toString());
				rs = pstmt.executeQuery();
				List<OrderLine> dataAux = new ArrayList<OrderLine>();
				// Itero por cada uno de los pedidos almacenando en dataAux
				// todas las lineas de todos los pedidos retornados en
				// la consulta anterior.
				while (rs.next()) {
					loadOrder(rs.getInt("C_Order_ID"), isForInvoice(), allowDeliveryReturned(), false);
					List<OrderLine> data = (List<OrderLine>) ((CreateFromTableModel) window.getDataTable().getModel()).getSourceEntities();
					Iterator<? extends SourceEntity> it = data.iterator();
					while (it.hasNext()) {
						dataAux.add((OrderLine) it.next());
					}
				}
				initDataTable();
				filtrarColumnaInstanceName(dataAux);
				loadTable(dataAux);

			} catch (Exception e) {
				log.log(Level.SEVERE, sql.toString(), e);
			} finally {
				try {
					if (rs != null)
						rs.close();
					if (pstmt != null)
						pstmt.close();
				} catch (Exception e) {
				}
			}
		}
		else {
			// Debe inicializarse nuevamente a fin de que se vacie la grilla
			initDataTable();
			window.tableChanged(null);
		}
	}

	// Si el check de mostrar todos los pedidos esta seleccionado crea el modelo
	// de tabla para líneas de remitos.
	// En caso contratio crea el modelo de tabla para líneas de documentos
	// (Pedidos, Remitos, Facturas)
	protected CreateFromTableModel createTableModelInstance() {
		if (!isSOTrx() && allInOut.isSelected()) {
			return new DocumentLineTableModelFromShipment();
		} else {
			return new DocumentLineTableModel();
		}
	}

	/**
	 * Modelo de tabla para líneas de remitos.
	 */
	public class DocumentLineTableModelFromShipment extends
			DocumentLineTableModel {
		// Constantes de índices de las columnas en la grilla.
		public static final int COL_IDX_LINE = 3;
		public static final int COL_IDX_ITEM_CODE = 4;
		public static final int COL_IDX_PRODUCT = 5;
		public static final int COL_IDX_UOM = 7;
		public static final int COL_IDX_QTY = 8;
		public static final int COL_IDX_REMAINING = 9;

		public static final int COL_IDX_ORDER = 1;
		public static final int COL_IDX_DATE = 2;
		public static final int COL_IDX_INSTANCE_NAME = 6;

		public int visibles = 10;

		@Override
		protected void setColumnNames() {
			setColumnName(COL_IDX_LINE, Msg.getElement(getCtx(), "Line"));
			setColumnName(COL_IDX_ITEM_CODE,
					Msg.translate(Env.getCtx(), "Value"));
			setColumnName(COL_IDX_PRODUCT,
					Msg.translate(Env.getCtx(), "M_Product_ID"));
			setColumnName(COL_IDX_UOM, Msg.translate(Env.getCtx(), "C_UOM_ID"));
			setColumnName(COL_IDX_QTY, Msg.translate(Env.getCtx(), "Quantity"));
			setColumnName(COL_IDX_REMAINING,
					Msg.translate(Env.getCtx(), "RemainingQty"));

			setColumnName(COL_IDX_ORDER, Msg.getElement(getCtx(), "C_Order_ID"));
			setColumnName(COL_IDX_INSTANCE_NAME,
					Msg.translate(Env.getCtx(), "Attributes"));
			setColumnName(COL_IDX_DATE,
					Msg.translate(Env.getCtx(), "DateOrdered"));
		}

		@Override
		protected void setColumnClasses() {
			setColumnClass(COL_IDX_LINE, Integer.class);
			setColumnClass(COL_IDX_ITEM_CODE, String.class);
			setColumnClass(COL_IDX_PRODUCT, String.class);
			setColumnClass(COL_IDX_UOM, String.class);
			setColumnClass(COL_IDX_QTY, BigDecimal.class);
			setColumnClass(COL_IDX_REMAINING, BigDecimal.class);

			setColumnClass(COL_IDX_ORDER, String.class);
			setColumnClass(COL_IDX_INSTANCE_NAME, String.class);
			setColumnClass(COL_IDX_DATE, Date.class);
		}

		@Override
		public Object getValueAt(int rowIndex, int colIndex) {
			OrderLine docLine = (OrderLine) getDocumentLine(rowIndex);
			Object value = null;
			switch (colIndex) {
			case COL_IDX_LINE:
				value = docLine.lineNo;
				break;
			case COL_IDX_ITEM_CODE:
				value = docLine.itemCode;
				break;
			case COL_IDX_PRODUCT:
				value = docLine.productName;
				break;
			case COL_IDX_UOM:
				value = docLine.uomName;
				break;
			case COL_IDX_QTY:
				value = docLine.lineQty;
				break;
			case COL_IDX_REMAINING:
				value = docLine.remainingQty;
				break;
			case COL_IDX_ORDER:
				value = docLine.documentNo;
				break;
			case COL_IDX_INSTANCE_NAME:
				value = docLine.instanceName;
				break;
			case COL_IDX_DATE:
				value = docLine.dateOrderLine;
				break;
			default:
				value = super.getValueAt(rowIndex, colIndex);
				break;
			}
			return value;
		}

		@Override
		public int getColumnCount() {
			return visibles;
		}
	}
	
	// El siguiente metodo podrá ser redefinido por un plugin para agregar una funcionalidad particular.
	// El metodo es invocado antes de hacer el save de la linea
	protected void customMethod(PO ol, PO iol) {
	} 
	
	@Override
	protected boolean lazyEvaluation() {
		return false;
	}

	protected Boolean getIsAllowDeliveryReturns() {
		return isAllowDeliveryReturns;
	}

	protected void setIsAllowDeliveryReturns(Boolean isAllowDeliveryReturns) {
		this.isAllowDeliveryReturns = isAllowDeliveryReturns;
	}

	protected MDocType getDocType() {
		return docType;
	}

	protected void setDocType(MDocType docType) {
		this.docType = docType;
	}
	
	public void showWindow()
	{
		window.setVisible(true);
	}
	
	public void closeWindow()
	{
		window.dispose();
	}

}