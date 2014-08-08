package hoover
import static com.example.BasicChecks.*

class FieldFormat {

    enum Type {
        STRING,DATE,TIME,INTEGER
    }

    enum Key  {
        TYPE, MAY_BE_EMPTY, PRIMARY_KEY
    }

    final Type type
    final boolean mayBeEmpty
    final boolean primaryKey
    boolean lastField
    
    FieldFormat(Type type) {
        this(type,[:])
    }

    FieldFormat(Type type, Map data) {
        checkNotNull(type,"type")
        this.type = type
        this.mayBeEmpty = FileFormat.fromMap(data, false, Key.MAY_BE_EMPTY)
        this.primaryKey = FileFormat.fromMap(data, false, Key.PRIMARY_KEY)
    }
}
