package practica5_6;

import java.util.*;

public class UsuarioTwitter implements Comparable<UsuarioTwitter>{
	protected String id;
	protected String screenName;// – nick de twitter (sin la arroba) 
	protected ArrayList<String> tags;// – lista de etiquetas 
	protected String avatar;// - URL del gráfico del avatar de ese usuario 
	protected Long followersCount;// - número de seguidores 
	protected Long friendsCount;// - número de amigos 
	protected String lang;// - idioma 
	protected Long lastSeen;// - fecha de última entrada en twitter (en milisegundos desde 1/1/1970) 
	protected String tweetId;// - identificador de tuit (ignorarlo) 
	protected ArrayList<String> friends;// - lista de amigos (expresados como ids de usuarios
	protected int amigosEnSistema;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getScreenName() {
		return screenName;
	}
	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}
	public ArrayList<String> getTags() {
		return tags;
	}
	public void setTags(ArrayList<String> tags) {
		this.tags = tags;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public Long getFollowersCount() {
		return followersCount;
	}
	public void setFollowersCount(Long followersCount) {
		this.followersCount = followersCount;
	}
	public Long getFriendsCount() {
		return friendsCount;
	}
	public void setFriendsCount(Long friendsCount) {
		this.friendsCount = friendsCount;
	}
	public String getLang() {
		return lang;
	}
	public void setLang(String lang) {
		this.lang = lang;
	}
	public Long getLastSeen() {
		return lastSeen;
	}
	public void setLastSeen(Long lastSeen) {
		this.lastSeen = lastSeen;
	}
	public String getTweetId() {
		return tweetId;
	}
	public void setTweetId(String tweetId) {
		this.tweetId = tweetId;
	}
	public ArrayList<String> getFriends() {
		return friends;
	}
	public void setFriends(ArrayList<String> friends) {
		this.friends = friends;
	}
	public int getAmigosEnSistema() {
		return amigosEnSistema;
	}
	public void setAmigosEnSistema(int amigosEnSistema) {
		this.amigosEnSistema = amigosEnSistema;
	}
	public UsuarioTwitter(String id,String screenName, ArrayList<String> tags, String avatar, Long followersCount,
			Long friendsCount, String lang, Long lastSeen, String tweetId, ArrayList<String> friends) {
		super();
		this.id = id;
		this.screenName = screenName;
		this.tags = tags;
		this.avatar = avatar;
		this.followersCount = followersCount;
		this.friendsCount = friendsCount;
		this.lang = lang;
		this.lastSeen = lastSeen;
		this.tweetId = tweetId;
		this.friends = friends;
		this.amigosEnSistema=0;
	}
	@Override
	public int compareTo(UsuarioTwitter o) {
		if (this.amigosEnSistema == o.amigosEnSistema) {
			return this.screenName.compareTo(o.screenName); 
		}else {
			return o.amigosEnSistema - this.amigosEnSistema;
		}
	}
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return Integer.parseInt(id);
	}
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if (obj == null) {
			return false;
		}else if (obj instanceof UsuarioTwitter) {
			return ((this.id.equals(((UsuarioTwitter)obj).id))&&(this.screenName.equals(((UsuarioTwitter)obj).screenName)));
		}else {
			return false;
		}
	}
	@Override
	public String toString() {
		return "UsuarioTwitter [id=" + id + ", screenName=" + screenName + ", tags=" + tags + ", avatar=" + avatar
				+ ", followersCount=" + followersCount + ", friendsCount=" + friendsCount + ", lang=" + lang
				+ ", lastSeen=" + lastSeen + ", tweetId=" + tweetId + ", friends=" + friends + ", amigosEnSistema=" + amigosEnSistema +"]";
	}

	
}
