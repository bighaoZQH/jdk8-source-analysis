package org.omg.DynamicAny.DynAnyFactoryPackage;


/**
* org/omg/DynamicAny/DynAnyFactoryPackage/InconsistentTypeCode.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from c:/jenkins/workspace/8-2-build-windows-x64-cygwin-sans-NAS/jdk8u381/237/corba/src/share/classes/org/omg/DynamicAny/DynamicAny.idl
* Wednesday, June 14, 2023 1:51:54 PM UTC
*/

public final class InconsistentTypeCode extends org.omg.CORBA.UserException
{

  public InconsistentTypeCode ()
  {
    super(InconsistentTypeCodeHelper.id());
  } // ctor


  public InconsistentTypeCode (String $reason)
  {
    super(InconsistentTypeCodeHelper.id() + "  " + $reason);
  } // ctor

} // class InconsistentTypeCode
