<@extends src="base.ftl">

<@block name="header">
<h1>Liste de postes ouverts à candidature</h1>
</@block>

<@block name="content">

<ul>
<#list jobs as job>
  <li>
    <div>
        <h2><a href="${baseUrl}site/recruitment/job/${job.id}/">${job.title} N°${job.job.number} (Section ${job.job.section})</a></h2>
    </div>
  </li>
</#list>
</ul>

</@block>
</@extends>
