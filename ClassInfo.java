//Author:   Brendon Tam
//Date  :   10/5/17
//Homework assignment: 3
//Objective: This program accepts a Class and command line arguments and displays the requested
//           info about the Class using professor's GetOpt class to parse command line and retrieve options.

import java.util.*;
import java.lang.reflect.*;

class ClassInfo 
{
    //******************************executeOptions()*********************************
    public static void executeOptions(GetOpt g)
    {
        int c;
        g.opterr(false);  // suppress display error messages 

        String t[] = g.getarg();
        String className = t[0];
		
        try
        {
            Class classObj = Class.forName(className); 
			
            while( (c = g.getopt()) != -1 )
            {
                switch(c)
                {
                    case 'c': listConstructors(classObj); break;
                    case 'm': listMethods(classObj); break;
                    case 'v': listVariables(classObj); break;
                    case 'C': listConstants(classObj); break;
                    case 'i': listInterfaces(classObj); break;
                    case 's': listSuperClass(classObj); break;
                    case 'a': listAll(classObj); break;
                    case 'h': help(); break;
                }
            }
            System.exit(0);
        }
        catch(Throwable e)
        {
            System.err.println(e);
        }
    }
    //*********************************help()**************************************
    public static void help()
    {
        System.out.println("This program accepts 'c', 'm', 'v','C', 'i', 's', 'a' as options");
        System.out.println("-c list all constructors for that Class");
        System.out.println("-m list all methods for that Class");
        System.out.println("-C list all Constants for that Class");
        System.out.println("-v list all Variables for that Class");
        System.out.println("-i list all Interfaces for that Class");
        System.out.println("-s list the SuperClass for that Class");
        System.out.println("-a display all of the above");
        System.exit(0);
    }

    //******************************listConstructors()********************************
    public static void listConstructors(Class classObj)
    {
        Constructor c[] = classObj.getDeclaredConstructors();

        System.out.println("Constructors");
        System.out.println("------------------");

        if(c.length == 0)
            System.out.println(classObj.getSimpleName() + " does not have any constructors");
        else
            for(int i =0; i < c.length; i++)    
            {
                String mod = Modifier.toString(c[i].getModifiers() );
                Parameter[] paramTypes = c[i].getParameters();  
                String name = classObj.getSimpleName();

                System.out.print(mod + " " + name + "(" );
               
                for(int k = 0; k < paramTypes.length; k++)
                {    
                    if(k > 0)
                        System.out.print(", ");
                    
                    System.out.print(paramTypes[k].getType().getSimpleName() );
                }
                System.out.println(");");   
            }    
               
        System.out.println();
    }

    //******************************listMethods()**********************************
    public static void listMethods(Class classObj)
    {
        Method m[] = classObj.getDeclaredMethods();

        System.out.println("Methods");
        System.out.println("------------------");

        if(m.length == 0)
            System.out.println(classObj.getSimpleName() + " does not have any Methods");
        else
            for(int i =0; i < m.length; i++)
            {
                Method m1 = m[i];
                Class returnType = m1.getReturnType();
                String strRetType = returnType.getSimpleName();
                Class[] paramTypes = m1.getParameterTypes();
                System.out.print(Modifier.toString(m1.getModifiers() ));
                System.out.print(" " + strRetType + " " + m1.getName() + "(" ) ;
                for(int k = 0; k < paramTypes.length; k++)
                {    
                    if(k > 0)
                        System.out.print(", ");
                    
                    System.out.print(paramTypes[k].getSimpleName() );
                }
                System.out.println(");");                 
            }
    }

    //*****************************listConstants()**********************************
    public static void listConstants(Class classObj)
    {
        Field f[] = classObj.getDeclaredFields();

        System.out.println("Constants");
        System.out.println("------------------");

        if(f.length == 0) 
            System.out.println(classObj.getSimpleName() + " does not have any Constants");

        else 
            for(int i = 0; i < f.length ; i++)
                if(f[i].toString().contains("final") ) 
                    displayConstHelper(f[i]);

        System.out.println(); //just to make output cleaner
    } 

    //****************************displayConstHelper()******************************
    private static void displayConstHelper(Field f)
    {  
        try
        {
            int mod = f.getModifiers() & Modifier.fieldModifiers();
            String modifier = Modifier.toString(mod);
            Class<?> type = f.getType();
            String typeName = type.getSimpleName();
            String fName = f.getName();

            f.setAccessible(true);
            System.out.println(modifier + " " + typeName + " " + fName + " = " + f.get(null));  
        } 
        catch (Exception e) 
        {
            System.out.println("Exception :" + e);
        }
    }

    //*****************************listVariables()**********************************
    public static void listVariables(Class classObj)
    {
        Field[] f = classObj.getDeclaredFields();

        System.out.println("Variables");
        System.out.println("------------------");

        if(f.length == 0) 
            System.out.println(classObj.getSimpleName() + " does not have any Variables");

        else 
            for(int i = 0; i < f.length ; i++)
                if(f[i].toString().contains("final") ) 
                    displayConstHelper(f[i]);
                else
                    displayVarHelper(f[i]);

        System.out.println(); //just to make output cleaner
    }

    //****************************displayVarHelper()********************************
    private static void displayVarHelper(Field f)
    {
        int mod = f.getModifiers() & Modifier.fieldModifiers();
        String modifier = Modifier.toString(mod);
        Class<?> type = f.getType();
        String typeName = type.getSimpleName();
        String fName = f.getName();

        System.out.println(modifier + " " + typeName + " " + fName); 
    }

    //*****************************listInterfaces()*********************************
    public static void listInterfaces(Class classObj)
    {
        System.out.println(classObj.getSimpleName() + " implements these Interfaces");
        System.out.println("-----------------");

        Class in[] = classObj.getInterfaces();

        if(in.length == 0 )
            System.out.println("None");
        else
            for(int i = 0; i < in.length; i++)
                System.out.println(in[i].getSimpleName() ) ;

        System.out.println(); //just to make output cleaner
    }

    //****************************listSuperClass()*********************************
    public static void listSuperClass(Class classObj) 
    {
        System.out.println("SuperClass");
        System.out.println("----------------");

        Class<?> superClass = classObj.getSuperclass();
        System.out.print(classObj.getSimpleName() + "'s superclass is ");
        System.out.println(superClass.getSimpleName() );
        System.out.println(); //just to make output cleaner
    }

    //*********************************listAll()**********************************
    public static void listAll(Class classObj)
    {
        listConstructors(classObj);
        listMethods(classObj); 
        listVariables(classObj); 
        listConstants(classObj); 
        listInterfaces(classObj); 
        listSuperClass(classObj);  
    }

    //******************************main()****************************************
    public static void main(String... args)
    {
        if(args.length == 0) 
            help();

        GetOpt g = new GetOpt(args, "cmCvisah");
        executeOptions(g);    
    }    
}

