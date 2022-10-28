package co.edu.unipiloto.forms;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RapiCoopDatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME="Usuarios.db";
    public static final String TABLE_NAME="Registros_RapiCoop";
    public static final String COL1="ID";
    public static final String COL2="FULLNAME";
    public static final String COL3="USERNAME";
    public static final String COL4="EMAIL";
    public static final String COL5="PASSWORD";
    public static final String COL6="TIPO_USUARIO";
    public static final String COL7="DIRECCION";
    public static final String COL8="ANO_NACIMIENTO";
    public static final String COL9="GENERO";
    private static final String DB_NAME="RapiCoop";
    private static final int DB_VERSION=1;

    public RapiCoopDatabaseHelper(Context context, String instituto, Object o, int i){
        super(context, DATABASE_NAME,null,1);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("create table "+TABLE_NAME+"(ID INTEGER PRIMARY KEY AUTOINCREMENT, "+
                "FULLNAME TEXT,USERNAME TEXT, EMAIL TEXT, PASSWORD TEXT, TIPO_USUARIO TEXT, DIRECCION TEXT, ANO_NACIMIENTO TEXT, GENERO TEXT) ");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1){
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(db);

    }
    public void initData(){
        SQLiteDatabase db= this.getWritableDatabase();
        onUpgrade(db,1,1);
    }

    public boolean insertData(User user){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put(COL2,user.getFullname());
        contentValues.put(COL3,user.getUserName());
        contentValues.put(COL4,user.geteMail());
        contentValues.put(COL5,user.getPassword());
        contentValues.put(COL6,user.getRolUsuario());
        contentValues.put(COL7,user.getDireccion());
        contentValues.put(COL8,user.getano_nacimiento());
        contentValues.put(COL9,user.getGenero());
        long result=db.insert(TABLE_NAME, null, contentValues);
        if(result==1)
            return false;
        else
            return true;
    }
    public Cursor getAlldata(){
        SQLiteDatabase db= this.getWritableDatabase();
        Cursor result = db.rawQuery("select * from "+TABLE_NAME,null);
        return result;
    }

    public Cursor getData(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from"+TABLE_NAME+"where id="+id+"",null);
        return res;
    }
    public Cursor getUser(String user){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from"+TABLE_NAME+"where COL3="+user+"",null);
        return res;
    }
    public Cursor findDataByEmail(String email){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from"+TABLE_NAME+"where "+COL4+"= '"+email+"'",null);
        return res;
    }
}
