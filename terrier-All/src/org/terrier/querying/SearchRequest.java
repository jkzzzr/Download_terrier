/*
 * Terrier - Terabyte Retriever
 * Webpage: http://terrier.org
 * Contact: terrier{a.}dcs.gla.ac.uk
 * University of Glasgow - School of Computing Science
 * http://www.gla.ac.uk/
 *
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See
 * the License for the specific language governing rights and limitations
 * under the License.
 *
 * The Original Code is SearchRequest.java.
 *
 * The Original Code is Copyright (C) 2004-2012 the University of Glasgow.
 * All Rights Reserved.
 *
 * Contributor(s):
  *   Craig Macdonald <craigm{a.}dcs.gla.ac.uk> (original author)
  *   Dyaa Albakour <dyaa{a.}dcs.gla.ac.uk>
 */
package org.terrier.querying;
import java.io.Serializable;
import org.terrier.matching.ResultSet;
import org.terrier.querying.parser.Query;
/** 
 * SearchRequst是Terrier框架在检索时，客户端可用的两个主要类中的一个。
 * 每个搜索查询，无论是用户出入还是在批量检索模式中输入，最终创建搜索请求。然后将每个搜索请求
 * 传递给正在处理每个搜索请求的Manager方法：runPreProcessing,runMatching,runPostProcess,runPostFilters
 * 使用示例：
 * <pre>
 * Index index = Index.createIndex();
 * Manager manager = new Manager(index);
 * SearchRequest srq = manager.newSearchRequest("my qurey");
 * m.runPrePocessing(srq);
 * manmager.runMatching(srq);
 * manager.runPostProcess(srq);
 * manager.runPostFilters(srq);
 * </pre>
 * <p><B>NB:Congtrols(name,value String tuples)用来控制查询流程。你可能想要在你的应用代码中设置controls,
 * 但是，备用控制在<tt>querying.default.controls</tt>属性在terrier.properties文件中设置。这些内容用来设置Terrier中
 * 类的任意信息。。
 * SearchRequest is the one of two main classes of which are made available to client code by
  * the Terrier framework at retrieval time. Each search query, whether entered by a user or
  * in batch mode retrieval creates a search request. Each search request is then passed to 4
  * methods of the Manager that is handling each search request: runPreProcessing, runMatching,
  * runPostProcess and runPostFilters
  * Example usage:
  * <pre>
  * Index index = Index.createIndex();
  * Manager manager = new Manager(index);
  * SearchRequest srq = manager.newSearchRequest("my query");
  * //run the query
  * m.runPreProcessing(srq);
  * manager.runMatching(srq);
  * manager.runPostProcess(srq);
  * manager.runPostFilters(srq);
  * </pre>
  * <P><B>NB:</B>Controls (name, value String tuples) are used to control the retrieval process. You may 
  * want to set controls in your application code. However, default controls can be set using 
  * the <tt>querying.default.controls</tt> property in the terrier.properties file.
  * <p><b>Context Objects (name, value object tuples) are used to pass arbitrary information to classes
  * within Terrier.
  */
public interface SearchRequest extends Serializable
{
	/** Set the matching model and weighting model that the Manager should use for this query.
	  * The Matching model  should be a subclass of org.terrier.matching.Matching, and
	  * the weighting model should implement org.terrier.matching.Model.<br>
	  * Example: <tt>request.addMatchingModel("Matching", "PL2")</tt>
	  * @param MatchingModelName the String class name that should be used
	  * @param WeightingModelName the String class name that should be used */
	void addMatchingModel(String MatchingModelName, String WeightingModelName);
	/** Set the query to be a parsed Query syntax tree, as generated by the Terrier query parser
	  * @param q The Query object syntax tree */
	void setQuery(Query q);
	/** Set a unique identifier for this query request.
	  * @param qid the unique string identifier*/
	void setQueryID(String qid);
	/** Get the Query syntax tree
	  * @return The query object as set with setQuery.
	  * */
	Query getQuery();
	/** Set a control named to have value Value. This is essentially a
	  * hashtable wrappers. Controls are used to set properties of the
	  * retrieval process.
	  * @param Name the name of the control to set the value of.
	  * @param Value the value that the control should take. */
	void setControl(String Name, String Value);
	/** Returns the query id as set by setQueryID(String).
	  * @return the query Id as a string. */
	String getQueryID();
	/** Returns the resultset generated by the query. Before retrieving this
	  * you probably need to have run Manager.runMatching, and (optionally, at
	  * your own risk) Manager.runPostProcesses() and Manager.runPostFiltering().
	  * @return ResultSet the resultset - ie the set of document Ids and their scores. */
	ResultSet getResultSet();
	/** Returns the value of the control. Null or empty string if not set.
	  * @return the value. */
	String getControl(String Name);
	/** Set if the query input had no terms.
	  * @return true if the query has no terms. Used by Manager.runMatching() to short circuit the
	  * matching process - if this is set, then a resultset with 0 documents is created
	  * automatically.
	  * - return true if the query has no terms */
	boolean isEmpty();

	/**
	 * sets the original query, before any preprocessing
	 */
	void setOriginalQuery(String q);
	
	/**
	 * gets the original query, before any preprocessing
	 */
	String getOriginalQuery();
	
	/**
	 * Sets the number of documents returned for a search request, after
	 * applying post filtering
	 * @param n
	 */
	void setNumberOfDocumentsAfterFiltering(int n);
	
	/**
	 * gets the number of documents returned for a search request, after
	 * applying post filtering
	 * @return the number of documents returned for a search request. integer.
	 */
	int getNumberOfDocumentsAfterFiltering();
	/**
	 * Returns the time the process start.
	 * @return time long
	 */
	long getStartedProcessingTime();
	
	/**
	 * Sets the started processing time.
	 * @param time
	 */
	void setStartedProcessingTime(long time);
	
	
	/**
	 *  Set a value of a context object.
	 * @param key the key of the context object
	 * @param value the value of the context object
	 * @since 3.6
	 */
	public void setContextObject(String key, Object value);
	
	/**
	 * Returns the value of a context object.
	 * @param key the key of the context object to get
	 * @return the value of the context object for the given key
	 * @since 3.6
	 */
	public Object getContextObject(String key);
	
}
/* the following methods are implmented in the SearchRequest - ie Request.java
   so are only visible from inside the querying package:
        public String getWeightingModel()
        public String getMatchingModel()
        public void setControl(String Name, String Value)
        public void setResultSet(ResultSet results)
        public ResultSet getResultSet()
        public String getControl(String Name)
        public Map<String,String> getControlHashtable()
        public boolean isEmpty()
        public void setEmpty(boolean in)
        public void setMatchingQueryTerms(MatchingQueryTerms mqts)
        public MatchingQueryTerms getMatchingQueryTerms()
*/