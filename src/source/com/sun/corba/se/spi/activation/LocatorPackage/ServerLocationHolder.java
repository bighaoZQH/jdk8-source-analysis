package com.sun.corba.se.spi.activation.LocatorPackage;

/**
* com/sun/corba/se/spi/activation/LocatorPackage/ServerLocationHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from c:/jenkins/workspace/8-2-build-windows-x64-cygwin-sans-NAS/jdk8u381/237/corba/src/share/classes/com/sun/corba/se/spi/activation/activation.idl
* Wednesday, June 14, 2023 1:51:54 PM UTC
*/

public final class ServerLocationHolder implements org.omg.CORBA.portable.Streamable
{
  public com.sun.corba.se.spi.activation.LocatorPackage.ServerLocation value = null;

  public ServerLocationHolder ()
  {
  }

  public ServerLocationHolder (com.sun.corba.se.spi.activation.LocatorPackage.ServerLocation initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = com.sun.corba.se.spi.activation.LocatorPackage.ServerLocationHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    com.sun.corba.se.spi.activation.LocatorPackage.ServerLocationHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return com.sun.corba.se.spi.activation.LocatorPackage.ServerLocationHelper.type ();
  }

}
