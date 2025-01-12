package com.volmit.iris.util.consts;

public class GitHub {
    private static final String dimensionPackName = "overworld";
    private static final String dimensionPackOrganization = "LizardNetworkMC";
    private static final String dimensionPackTag = "v3.1.0";

    public static String getDimensionPackName() {
        return dimensionPackName;
    }

    public static String getDimensionPackOrganization() {
        return dimensionPackOrganization;
    }

    public static String getDimensionPackTag() {
        return dimensionPackTag;
    }

    public static String getDimensionPackRepo() {
        return String.format("%s/%s", dimensionPackOrganization, dimensionPackName);
    }

    public static String getDimensionPackBaseUrl() {
        return String.format("https://github.com/%s/%s", dimensionPackOrganization, dimensionPackName);
    }

    public static String getDimensionPackArchiveUrl() {
        return String.format("%s/archive/refs/tags/%s.zip", getDimensionPackBaseUrl(), dimensionPackTag);
    }
}