package org.polyglotted.attributerepo.spring;

import java.util.Properties;

import org.springframework.core.io.Resource;

class OverrideFileLoadVisitor {

    private Resource overrideFileLocation;

    public void setOverrideFileLocation(Resource overrideFileLocation) {
        this.overrideFileLocation = overrideFileLocation;
    }

    public boolean visit(Properties props) {
        return PropertyLoaderUtils.safeLoad(props, overrideFileLocation);
    }
}
