package gov.nist.sip.proxy.additionalServices;

public class FriendsCharger extends Charger {
	int FR_COST = 2;
	int FA_COST = 1;
	int DEFAULT_COST = 10;
	
	@Override
	public long charge(String username, String to_user, long duration) {
		String relation = mBillingDB.getRelation(username, to_user);
		if (relation.equals("friends"))
			return FR_COST*duration; 
		else if(relation.equals("family")){
			return FA_COST*duration; 
		}
		else{
			return DEFAULT_COST*duration; 
		}
	}
}
