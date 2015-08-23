package name.heavycarbon.utils;

/* 34567890123456789012345678901234567890123456789012345678901234567890123456789
 *******************************************************************************
 * An interface for something that can create a subclass of AbstractId
 * 
 * 2009.12.18 - Moved out of RangeSequence to its own toplevel class
 * 2010.09.27 - Moved from the specialized project "70_msgserver_cli" 
 *              to project "04_core_low" and package "com.mplify.id_ranges".
 ******************************************************************************/

public interface IdFactory<T extends AbstractId> {
    
    public T make(Integer id);
    
}