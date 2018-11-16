package org.craftercms.commons.file.stores.impl;

import org.craftercms.commons.config.ConfigurationException;
import org.craftercms.commons.config.ConfigurationProfile;
import org.craftercms.commons.config.ConfigurationProfileLoader;
import org.springframework.beans.factory.annotation.Required;

import java.io.IOException;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractProfileAwareRemoteFileStore<T extends ConfigurationProfile> extends AbstractRemoteFileStore {

    private static final String DEFAULT_REMOTE_ID_WITH_PROFILE_PATTERN = "^/?([^/]+)/(.+)$";
    private static final int DEFAULT_PROFILE_ID_MATCH_GROUP = 1;
    private static final int DEFAULT_ACTUAL_REMOTE_ID_MATCH_GROUP = 2;

    protected Pattern remoteIdWithProfilePattern;
    protected int profileIdMatchGroup;
    protected int actualRemoteIdMatchGroup;
    protected ConfigurationProfileLoader<T> profileLoader;

    public AbstractProfileAwareRemoteFileStore() {
        remoteIdWithProfilePattern = Pattern.compile(DEFAULT_REMOTE_ID_WITH_PROFILE_PATTERN);
        profileIdMatchGroup = DEFAULT_PROFILE_ID_MATCH_GROUP;
        actualRemoteIdMatchGroup = DEFAULT_ACTUAL_REMOTE_ID_MATCH_GROUP;
    }

    public void setRemoteIdWithProfilePattern(String remoteIdWithProfilePattern) {
        this.remoteIdWithProfilePattern = Pattern.compile(remoteIdWithProfilePattern);
    }

    public void setProfileIdMatchGroup(int profileIdMatchGroup) {
        this.profileIdMatchGroup = profileIdMatchGroup;
    }

    public void setActualRemoteIdMatchGroup(int actualRemoteIdMatchGroup) {
        this.actualRemoteIdMatchGroup = actualRemoteIdMatchGroup;
    }

    @Required
    public void setProfileLoader(ConfigurationProfileLoader<T> profileLoader) {
        this.profileLoader = profileLoader;
    }

    @Override
    protected void doDownload(String remoteId, Path downloadPath) throws IOException {
        Matcher matcher = remoteIdWithProfilePattern.matcher(remoteId);
        if (matcher.matches()) {
            String profileId = matcher.group(profileIdMatchGroup);
            String actualRemoteId = matcher.group(actualRemoteIdMatchGroup);

            try {
                doDownload(profileLoader.loadProfile(profileId), actualRemoteId, downloadPath);
            } catch (ConfigurationException e) {
                throw new IOException("Unable to load configuration profile with ID " + profileId);
            }
        } else {
            throw new IllegalArgumentException("Remote ID " + remoteId + " expected to match pattern " +
                                               remoteIdWithProfilePattern);
        }
    }

    protected abstract void doDownload(T profile, String remoteId, Path downloadPath) throws IOException;

}
