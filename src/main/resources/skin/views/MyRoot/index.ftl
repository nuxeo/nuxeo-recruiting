<@extends src="base.ftl">
<@block name="header">You signed in as ${Context.principal}</@block>

<@block name="content">

<h1>Job listing</h1>

<ol>
<#list jobs as job>
  <li>
    <div>
        <h2><a href="${baseUrl}site/recruitment/job/${job.id}/">${job.title} (${job.job.number})</a></h2>
    </div>
  </li>
</#list>
</ol>

</@block>
</@extends>
