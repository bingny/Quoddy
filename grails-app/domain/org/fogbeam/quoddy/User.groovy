package org.fogbeam.quoddy;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.fogbeam.quoddy.profile.Profile;
import org.fogbeam.quoddy.stream.StatusUpdate;

@XmlRootElement
@XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.NONE)
class User implements Serializable
{

	public User()
	{
		this.uuid = java.util.UUID.randomUUID().toString();
	}
	
    static constraints = {
        userId( size:3..20, unique:true )
        homepage( url:true, nullable:true )
        validator: {passwd, user -> 
                return passwd != user.userId 
            }
        profile( nullable: true )
        currentStatus(nullable:true)
		email(nullable:true)
		dateCreated()
    }
	
	public String toString()
	{
		return "id: ${id} uuid: ${uuid}, userId: ${userId}, password: ${password}, firstName: ${firstName}, lastName: ${lastName}, homepage: ${homepage}";
		
	}
	
	@XmlElement
    String uuid;
	@XmlElement
	String userId;
	@XmlElement
    Date dateCreated;
    Profile profile;
    StatusUpdate currentStatus;	
	
	/* stuff objects of this class "carry around" but aren't persisted as part of the object. 
	 * This stuff is pulled in from an external source, like, say, LDAP. */
	String password;
	@XmlElement
	String homepage;
	@XmlElement
	String firstName;
	@XmlElement
	String lastName;
	@XmlElement
	String displayName;
	// String fullName;  // see below
	String bio;
	@XmlElement
	String email;
	static transients = [ "password", "homepage", "displayName", "bio", "templateName" ]
	
    static mapping = {
    	table 'uzer'
		currentStatus lazy:false; // eagerly fetch the currentStatus
    	roles lazy: false;		  // eagerly fetch roles
		permissions lazy:false;   // eagerly fetch permissions
	}
	
	static fetchMode = [roles: 'eager', permissions:'eager'];
	
    static hasMany = [oldStatusUpdates:StatusUpdate, roles: AccountRole, permissions: String]
	static mappedBy = [oldStatusUpdates:'creator']

	
    public void setUuid( String uuid ){
    	
    	// never overwrite existing uuid value with NULL
    	if( uuid != null )
    	{
    		this.uuid = uuid;
    	}
    }

	public String getFullName()
	{
		return firstName + " " + lastName;	
	}	

	public void setFullName( String fullName )
	{}	
	
	public String getTemplateName()
	{
		return "/renderUser";
	}
	
}
