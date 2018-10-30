package org.personalized.dashboard.model;

import com.google.api.client.util.Lists;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by sudan on 6/4/15.
 */
@XmlRootElement
public class User {

    private final boolean isAdmin = false;
    private String userId;
    private String username;
    private String email;
    private String profilePicURL;
    private List<String> tags = Lists.newArrayList();
    private List<String> expenseCategories = Lists.newArrayList();

    public User() {

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfilePicURL() {
        return profilePicURL;
    }

    public void setProfilePicURL(String profilePicURL) {
        this.profilePicURL = profilePicURL;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<String> getExpenseCategories() {
        return expenseCategories;
    }

    public void setExpenseCategories(List<String> expenseCategories) {
        this.expenseCategories = expenseCategories;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("userId", userId)
                .append("username", username)
                .append("email", email)
                .append("profilePicURL", profilePicURL)
                .append("tags", tags)
                .append("expenseCategories", expenseCategories)
                .toString();
    }
}
