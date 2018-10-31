-- **********************
-- REQUESTED QUERIES:
-- **********************

-- .............................................
-- QUERY (1) Write MySQL query to find IPs that made more than a certain number of requests for a given time period.

SELECT 
	DISTINCT(rqOutter.DS_IP)
FROM
	PS_REQUEST rqOutter
WHERE
	(
		SELECT 
			COUNT(*) 
		FROM 
			PS_REQUEST rqInner 
		WHERE 
			rqInner.DS_IP = rqOutter.DS_IP 
		AND 
			rqInner.DT_DATE >= '2017-01-01.13:00:00' 
		AND 
			rqInner.DT_DATE <= '2017-01-01.13:59:59'
	) > 100;

-- where 100 is the number of requests, 2017-01-01.13:00:00 is the initial date and 2017-01-01.13:59:59 is the final date.
	
-- .............................................
-- QUERY (2) - "Write MySQL query to find requests made by a given IP."

SELECT * FROM PS_REQUEST WHERE DS_IP = '192.168.228.188';

-- where 192.168.228.188 is the desired IP address.