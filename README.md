# Ozone Metrics 
 
## Description

Ozone Metrics is a web service used to collect and aggregate data from a variety of sources, including the Ozone Widget Framework (OWF).
 
## Copyrights
> Software (c) 2012 [Next Century Corporation](http://www.nextcentury.com/ "Next Century")

> Portions (c) 2009 [TexelTek Inc.](http://www.texeltek.com/ "TexelTek")

> The United States Government has unlimited rights in this software, pursuant to the contracts under which it was developed.  
 
Ozone Metrics is released to the public as Open Source Software, because it's the Right Thing To Do. Also, it was required by [Section 924 of the 2012 National Defense Authorization Act](http://www.gpo.gov/fdsys/pkg/PLAW-112publ81/pdf/PLAW-112publ81.pdf "NDAA FY12").

Released under the [Apache License, Version 2](http://www.apache.org/licenses/LICENSE-2.0.html "Apache License v2").
 
## Community

### Google Groups

[ozoneplatform-users](https://groups.google.com/forum/?fromgroups#!forum/ozoneplatform-users) : This list is for users, for questions about the platform, for feature requests, for discussions about the platform and its roadmap, etc.

[ozoneplatform-dev](https://groups.google.com/forum/?fromgroups#!forum/ozoneplatform-dev) : This list is for the development community interested in extending or contributing to the platform.

[ozoneplatform-announcements](https://groups.google.com/forum/?fromgroups#!forum/ozoneplatform-announce) : This list is for announcements as new versions are released, technology refreshes are performed, or other relevant information as needed to be released.
 
### OWF GOSS Board
OWF started as a project at a single US Government agency, but developed into a collaborative project spanning multiple federal agencies.  Overall project direction is managed by "The OWF Government Open Source Software Board"; i.e. what features should the core team work on next, what patches should get accepted, etc.  Gov't agencies wishing to be represented on the board should check http://owfgoss.org for more details.  Membership on the board is currently limited to Government agencies that are using OWF and have demonstrated willingness to invest their own energy and resources into developing it as a shared resource of the community.  At this time, the board is not considering membership for entities that are not US Government Agencies, but we would be willing to discuss proposals.
 
### Contributions
#### Non-Government
Contributions to the baseline project from outside the US Federal Government should be submitted as a pull request to the core project on GitHub.  Before patches will be accepted by the core project, contributors have a signed Contributor License Agreement on file with the core team.  If you or your company wish your copyright in your contribution to be annotated in the project documentation (such as this README), then your pull request should include that annotation.
 
#### Government
Contributions from government agencies do not need to have a CLA on file, but do require verification that the government has unlimited rights to the contribution.  An email to goss-support@owfgoss.org is sufficient, stating that the contribution was developed by an employee of the United States Government in the course of his or her duties. Alternatively, if the contribution was developed by a contractor, the email should provide the name of the Contractor, Contract number, and an assertion that the contract included the standard "Unlimited rights" clause specified by [DFARS 252.227.7014](http://www.acq.osd.mil/dpap/dars/dfars/html/current/252227.htm#252.227-7014) "Rights in noncommercial computer software and noncommercial computer software documentation".
 
Government agencies are encouraged to submit contributions as pull requests on GitHub.  If your agency cannot use GitHub, contributions can be emailed as patches to goss-support@owfgoss.org.
 
### Roadmap
 
There is work underway to re-factor OWF to use OSGi on the back end, and eliminate the dependency on Ext JS for the front-end.  As part of this work, Ozone Metrics will become a module in this new architecture.  Multiple alphas have been released, and announced on [ozoneplatform-announce](https://groups.google.com/forum/?fromgroups#!forum/ozoneplatform-announce).  Although we initially intended a release for June 2013, we're revisiting our design to make sure it is fully scalable and enterprise-ready, both for OWF itself and for other capabilities built on top of the services of what we're calling _ozoneplatform_.  Watch [ozoneplatform-announce](https://groups.google.com/forum/?fromgroups#!forum/ozoneplatform-announce) for further information as to specific timelines and availability of alphas.
