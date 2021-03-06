/*
 *    El contenido de este fichero está sujeto a la  Licencia Pública openXpertya versión 1.1 (LPO)
 * en tanto en cuanto forme parte íntegra del total del producto denominado:  openXpertya, solución 
 * empresarial global , y siempre según los términos de dicha licencia LPO.
 *    Una copia  íntegra de dicha  licencia está incluida con todas  las fuentes del producto.
 *    Partes del código son CopyRight (c) 2002-2007 de Ingeniería Informática Integrada S.L., otras 
 * partes son  CopyRight (c) 2002-2007 de  Consultoría y  Soporte en  Redes y  Tecnologías  de  la
 * Información S.L.,  otras partes son  adaptadas, ampliadas,  traducidas, revisadas  y/o mejoradas
 * a partir de código original de  terceros, recogidos en el  ADDENDUM  A, sección 3 (A.3) de dicha
 * licencia  LPO,  y si dicho código es extraido como parte del total del producto, estará sujeto a
 * su respectiva licencia original.  
 *     Más información en http://www.openxpertya.org/ayuda/Licencia.html
 */



package org.openXpertya.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Properties;

import org.openXpertya.util.Env;

/**
 * Descripción de Clase
 *
 *
 * @version    2.2, 12.10.07
 * @author     Equipo de Desarrollo de openXpertya    
 */

public class MDistributionListLine extends X_M_DistributionListLine {

    /**
     * Constructor de la clase ...
     *
     *
     * @param ctx
     * @param M_DistributionListLine_ID
     * @param trxName
     */

    public MDistributionListLine( Properties ctx,int M_DistributionListLine_ID,String trxName ) {
        super( ctx,M_DistributionListLine_ID,trxName );
    }    // MDistributionListLine

    /**
     * Constructor de la clase ...
     *
     *
     * @param ctx
     * @param rs
     * @param trxName
     */

    public MDistributionListLine( Properties ctx,ResultSet rs,String trxName ) {
        super( ctx,rs,trxName );
    }    // MDistributionListLine

    /**
     * Descripción de Método
     *
     *
     * @return
     */

    public BigDecimal getMinQty() {
        BigDecimal minQty = super.getMinQty();

        if( minQty == null ) {
            return Env.ZERO;
        }

        return minQty;
    }    // getMinQty

    /**
     * Descripción de Método
     *
     *
     * @return
     */

    public BigDecimal getRatio() {
        BigDecimal ratio = super.getRatio();

        if( ratio == null ) {
            return Env.ZERO;
        }

        return ratio;
    }    // getRatio
}    // MDistributionListLine



/*
 *  @(#)MDistributionListLine.java   02.07.07
 * 
 *  Fin del fichero MDistributionListLine.java
 *  
 *  Versión 2.2
 *
 */
