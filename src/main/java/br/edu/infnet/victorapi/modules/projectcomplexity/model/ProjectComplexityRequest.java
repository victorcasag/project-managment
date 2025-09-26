package br.edu.infnet.victorapi.modules.projectcomplexity.model;

public class ProjectComplexityRequest {
    private int teamSize;
    private int weeksDuration;
    private int externalIntegrationsCount;
    private boolean strictDeadline;

    public ProjectComplexityRequest() {}

    public ProjectComplexityRequest(int teamSize, int weeksDuration, int externalIntegrationsCount, boolean strictDeadline) {
        this.teamSize = teamSize;
        this.weeksDuration = weeksDuration;
        this.externalIntegrationsCount = externalIntegrationsCount;
        this.strictDeadline = strictDeadline;
    }

    public int getTeamSize() {
        return teamSize;
    }

    public void setTeamSize(int teamSize) {
        this.teamSize = teamSize;
    }

    public int getWeeksDuration() {
        return weeksDuration;
    }

    public void setWeeksDuration(int weeksDuration) {
        this.weeksDuration = weeksDuration;
    }

    public int getExternalIntegrationsCount() {
        return externalIntegrationsCount;
    }

    public void setExternalIntegrationsCount(int externalIntegrationsCount) {
        this.externalIntegrationsCount = externalIntegrationsCount;
    }

    public boolean isStrictDeadline() {
        return strictDeadline;
    }

    public void setStrictDeadline(boolean strictDeadline) {
        this.strictDeadline = strictDeadline;
    }
}
