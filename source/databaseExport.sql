--
-- PostgreSQL database dump
--

-- Dumped from database version 13.5 (Ubuntu 13.5-2.pgdg20.04+1)
-- Dumped by pg_dump version 13.2

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: update_owner_client(); Type: FUNCTION; Schema: public; Owner: xlsztrkmmnwlxj
--

CREATE FUNCTION public.update_owner_client() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    IF NEW.usertype = 'owner' THEN
        insert into parkingowner(userid, iban, idpicture) values (NEW.userid, 'HR0000000000000000000', '');
    else
        insert into client(userid, walletbalance) values (NEW.userid, 0);
    end if;
    RETURN new;
END;
$$;


ALTER FUNCTION public.update_owner_client() OWNER TO xlsztrkmmnwlxj;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: client; Type: TABLE; Schema: public; Owner: xlsztrkmmnwlxj
--

CREATE TABLE public.client (
    userid integer NOT NULL,
    walletbalance numeric NOT NULL
);


ALTER TABLE public.client OWNER TO xlsztrkmmnwlxj;

--
-- Name: clientreservation; Type: TABLE; Schema: public; Owner: xlsztrkmmnwlxj
--

CREATE TABLE public.clientreservation (
    clientuserid integer NOT NULL,
    owneruserid integer NOT NULL,
    parkingspotnumber integer NOT NULL,
    timeofstart timestamp without time zone NOT NULL,
    duration integer NOT NULL,
    recurring boolean NOT NULL,
    CONSTRAINT clientreservation_duration_check CHECK (((duration >= 0) AND (duration <= (24 * 7))))
);


ALTER TABLE public.clientreservation OWNER TO xlsztrkmmnwlxj;

--
-- Name: hibernate_sequence; Type: SEQUENCE; Schema: public; Owner: xlsztrkmmnwlxj
--

CREATE SEQUENCE public.hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.hibernate_sequence OWNER TO xlsztrkmmnwlxj;

--
-- Name: parking; Type: TABLE; Schema: public; Owner: xlsztrkmmnwlxj
--

CREATE TABLE public.parking (
    userid integer NOT NULL,
    parkingname character varying(50) NOT NULL,
    parkingphoto character varying(50),
    hourlyprice numeric,
    description character varying(1000),
    entrancepointx numeric,
    entrancepointy numeric
);


ALTER TABLE public.parking OWNER TO xlsztrkmmnwlxj;

--
-- Name: parkingowner; Type: TABLE; Schema: public; Owner: xlsztrkmmnwlxj
--

CREATE TABLE public.parkingowner (
    userid integer NOT NULL,
    iban character(21) NOT NULL,
    idpicture character varying(50) NOT NULL
);


ALTER TABLE public.parkingowner OWNER TO xlsztrkmmnwlxj;

--
-- Name: parkingspot; Type: TABLE; Schema: public; Owner: xlsztrkmmnwlxj
--

CREATE TABLE public.parkingspot (
    userid integer NOT NULL,
    parkingspotnumber integer NOT NULL,
    parkingspottype character varying(20) NOT NULL,
    canbereserved boolean NOT NULL,
    point1x numeric,
    point1y numeric,
    point2x numeric,
    point2y numeric,
    point3x numeric,
    point3y numeric,
    point4x numeric,
    point4y numeric,
    CONSTRAINT parkingspot_parkingspottype_check CHECK ((upper((parkingspottype)::text) = ANY (ARRAY['BIKE'::text, 'CAR'::text])))
);


ALTER TABLE public.parkingspot OWNER TO xlsztrkmmnwlxj;

--
-- Name: parkingspotoccupancy; Type: TABLE; Schema: public; Owner: xlsztrkmmnwlxj
--

CREATE TABLE public.parkingspotoccupancy (
    userid integer NOT NULL,
    parkingspotnumber integer NOT NULL,
    datefrom timestamp without time zone NOT NULL,
    dateto timestamp without time zone,
    occupancy boolean NOT NULL
);


ALTER TABLE public.parkingspotoccupancy OWNER TO xlsztrkmmnwlxj;

--
-- Name: usertable; Type: TABLE; Schema: public; Owner: xlsztrkmmnwlxj
--

CREATE TABLE public.usertable (
    userid integer NOT NULL,
    username character varying(35) NOT NULL,
    userfirstname character varying(35) NOT NULL,
    usersurname character varying(35) NOT NULL,
    useremail character varying(50) NOT NULL,
    password character varying(256) NOT NULL,
    usertype character varying(15) NOT NULL,
    confirmed boolean NOT NULL
);


ALTER TABLE public.usertable OWNER TO xlsztrkmmnwlxj;

--
-- Data for Name: client; Type: TABLE DATA; Schema: public; Owner: xlsztrkmmnwlxj
--

COPY public.client (userid, walletbalance) FROM stdin;
2	0
4	0
8	0
10	0
11	0
14	0
16	0
17	0
19	0
20	0
22	0
29	0
30	0
34	0
35	0
\.


--
-- Data for Name: clientreservation; Type: TABLE DATA; Schema: public; Owner: xlsztrkmmnwlxj
--

COPY public.clientreservation (clientuserid, owneruserid, parkingspotnumber, timeofstart, duration, recurring) FROM stdin;
\.


--
-- Data for Name: parking; Type: TABLE DATA; Schema: public; Owner: xlsztrkmmnwlxj
--

COPY public.parking (userid, parkingname, parkingphoto, hourlyprice, description, entrancepointx, entrancepointy) FROM stdin;
23	Moj super parking		26	nije skup dobar je	15.973634232820949	45.80094454693843
\.


--
-- Data for Name: parkingowner; Type: TABLE DATA; Schema: public; Owner: xlsztrkmmnwlxj
--

COPY public.parkingowner (userid, iban, idpicture) FROM stdin;
5	HR159652655878955456 	
6	HR0000000000000000000	
7	HR1461894561816887   	
9	HR0000000000000000000	
12	HR2031564615185313   	
13	HR12345              	
15	HR1111111111111111111	
18	HR6023600003118286420	
21	HR1023900013102359906	
23	HR0000000000000000000	
24	HR0000000000000000000	
25	HR0000000000000000000	
31	HR6487461575854458858	
32	HR5647614537485124855	
33	HR0000000000000000000	
\.


--
-- Data for Name: parkingspot; Type: TABLE DATA; Schema: public; Owner: xlsztrkmmnwlxj
--

COPY public.parkingspot (userid, parkingspotnumber, parkingspottype, canbereserved, point1x, point1y, point2x, point2y, point3x, point3y, point4x, point4y) FROM stdin;
\.


--
-- Data for Name: parkingspotoccupancy; Type: TABLE DATA; Schema: public; Owner: xlsztrkmmnwlxj
--

COPY public.parkingspotoccupancy (userid, parkingspotnumber, datefrom, dateto, occupancy) FROM stdin;
\.


--
-- Data for Name: usertable; Type: TABLE DATA; Schema: public; Owner: xlsztrkmmnwlxj
--

COPY public.usertable (userid, username, userfirstname, usersurname, useremail, password, usertype, confirmed) FROM stdin;
6	markisa01	Marko	Žura	markozura@windowslive.com	$2a$10$jzFcDVmAo3W16.oUFpu6DunluVHQDdX8G5etm4gdw.1E7fGdcGUqG	owner	t
16	fikayo	Filip	Bura	filip.bura2408@gmail.com	$2a$10$KOng8qjQeMPkeYvmXL00COwKjMSG064IAr29ptYanrCjAnmtw8yTW	client	t
17	test1	test	1	1@test	$2a$10$wDOXruHqi5F08X3d38Y4e.maIudz8H42qgRwSjhiyEvB0BSYHZSaW	client	f
18	test2	test	2	2@test	$2a$10$IK7PG0j3zleaqfb6Ka1VzuoB2yXCiOCT88HNGmVVDZBBZXy2QDJxS	owner	t
12	PuhoOP	Marin	Puharic	marin@marin	$2a$10$Pwdd461.737xWlf5u1XRE.MZUjb5.4ZyIcL/nEQOHDZH0rrUYCKLG	owner	t
4	adminmoment	Kim	Staničić	kimica.s811@gmail.com	$2a$10$tiEEU93oFH.Y1Q4P7zemJuXO1j8d48fidHYQGA5amWxn5IYJ.09Ri	client	t
2	TestClient	testChanged	testName	test@client	$2a$10$PMup3fQK4I/HtZsJyvPB9.VKr3M9RSz9cGKdCHHIhhino8GEi0DJK	client	t
7	baca55	Miodrag	Radonjić	baca@jv.rs	$2a$10$HJ82Gsj67f7qCDuCfzzZEe4p3sXviAZ5WtJdx5/jqshmKgO7DbK4q	owner	t
8	marko_z1234	Marko	Žura	marko.zura@fer.hr	$2a$10$41L/9AZw.olsz3t.YJ5.TeI7QRobRqcjsa4rn81xHs/aj32roJEtC	client	t
9	golub	Golub	Golupčić	golub@jv.com	$2a$10$OFsj/rFwYGl605bReBigT..CDFLUD6KWzBVIZou5oT6916yYYDcbS	owner	t
10	marin	marin	puharic	mp52219@fer.hr	$2a$10$t2tsxu5Oe7FNEvJhFlbEeuIn81GpQt5YbZ9vD7ZTzA5T60w83snAa	client	t
11	Zvone1	Zvonimir	Ravlić	zvonimir31ravlic@gmail.com	$2a$10$1obYKNqhpiXrfrqwn6YQiODc20ZoRGyZx6ujBB8I6Vv1dDLTm5bsS	client	t
31	saravoditeljica	Sara	Para	sara@koko.com	$2a$10$/iiwSc/kgLMfFgZ.Tg9tl.UcoRdye78XsZqADDYOxUYnJQywYnc2a	owner	t
20	klijent235	klijent	2	2@klijent	$2a$10$.97gIImFjfU40q5Xfrk/iuMwcJVZ0nl.Xq6tXR8jLpnQqbuB.9tpS	client	t
13	mate1	Mate	Ivić	mateivic3007@gmail.com	$2a$10$7BwFKbxN4rNW//m4CNLlPuR7f/dbL5w2cJX8u6hNlNUtogUBwLdvW	owner	t
15	zvone31	Zvonimir	Ravlić	zvonimir13ravlic@gmail.com	$2a$10$zs/QoOy7GoDiK4oBwQQVLu0ZNdZPioWmACi9PiRihWROdgCt5X/gO	owner	t
32	LeoParkingOwner	Leo	Parking	leo@parking.znj	$2a$10$a.W5vG.Oq0kfEt94RSxbJ.mPaS6xPQU1SOAQpby9pDS9zSeSzzS26	owner	t
19	klijent1	klijent	1	1@klijent	$2a$10$ZhBLK5/gyERK7fejd4vZN./xg36SDthENkU3cjPrl6QhWhtxNXEtK	client	t
21	vlasnikTest	Vlasnik	Vlasnic	vlasnik@mail	$2a$10$D.6POw5zkRUeonw0acXMXe8qR4YveEAwePxW.duqiVRrE8hdOz3NG	owner	t
22	klijent3	klijent	3	3@klijent	$2a$10$i2RWh/gHR8Nj9mezkZ3Vq.K1eyHLXvANoP/Tl/nLQb7HQcDfD2hTe	client	t
23	vlasnik1	vlasnik	1	1@vlasnik	$2a$10$x6o965Ji2Yi1ShwCW3OLyOfNa89.OUI2nrRuW9g2A5TDI9Lx7Imhe	owner	t
24	vlasnik2	vlasnik	2	2@vlasnik	$2a$10$NNM0QV8KluoLGz0bo./9g.jvyteve.aGmm0eIdTA.6v82PgG0X1c2	owner	t
25	vlasnik3	vlasnik	3	3@vlasnik	$2a$10$G5zb/r7IzIIHtMYpinEjreHLNLmDe9Ic/FaOJxliUD5d1wzwwyjTS	owner	t
26	admin1	admin	1	1@admin	$2a$10$1PnKO1/6a8omyUDEMdrBEukAjinTH/zHLpk23TMtoFOf9/knRIPZy	admin	t
27	admin2	admin	2	2@admin	$2a$10$sAuE4klc6D5ecJFl5t/s2e0JdclvtUVdEWAIvsFEWMCpuQUIsCIDS	admin	t
28	admin3	admin	3	3@admin	$2a$10$5lhHygrGuiDzMZ41Fj4LI.NAQHRsI9wzCO6s8XqRK4pDNSrYt0pdW	admin	t
29	Bruno	Bruno	Milković	bruno.milkovic29@gmail.com	$2a$10$aNVZ5a5CpyTIyld2HBqzjOEh5qCnR/SytEasg0.4m6CkMit8MpBRS	client	t
30	josip	Josip	Srzić	josip.srzic@gmail.com	$2a$10$6wG/igKkTU5.1D1.Oa0ABuIAwlebZC3xiMIh36V5muP6fJTLP//7K	client	t
14	mate5	Mate	ivić	mate.ivic@hotmail.co.uk	$2a$10$93hpCTo8.hKVZG/ytWpLzuexpl6VK./twTHx4q1zRyAsIgSa7H7Jq	client	t
3	admin	Luka	Gjurić	dcryophoenix@gmail.com	$2a$10$a5jZxg/FDKLpCxA0e2x6Z.x682hJp6eBOyRXSwttsnbjWlDQgPdui	admin	t
5	voditelj1	Petar	Maraš	proba@gma.com	$2a$10$yvb0ORtgpniKP5zj2r2UNuCfETlEr9Ntlgczfhx7g6Avq/rn0c68G	owner	t
33	puhoReal	Marin	Puharic	puho@je.retard	$2a$10$JbdcAgvcuwWfXRdhaBjYYeScEy57qalDTxb8wlrSrcs0fhqOUkzNq	owner	f
34	zvonimir31ravlic@gmail.com				$2a$10$YYzJN/pJGiLW9h2ieab4/uSqz1G8MlnZYaQKF0LuE7i.ackjw6VSa	client	f
35	Mate123456	Marko	Stipic	marko1407stipic@gmail.com	$2a$10$RFo4WvobzM9b0lBiMn3D5ejdAQ8mrd9L2zoxEtvJecb1aszOMjlpi	client	f
\.


--
-- Name: hibernate_sequence; Type: SEQUENCE SET; Schema: public; Owner: xlsztrkmmnwlxj
--

SELECT pg_catalog.setval('public.hibernate_sequence', 35, true);


--
-- Name: usertable User_pkey; Type: CONSTRAINT; Schema: public; Owner: xlsztrkmmnwlxj
--

ALTER TABLE ONLY public.usertable
    ADD CONSTRAINT "User_pkey" PRIMARY KEY (userid);


--
-- Name: usertable User_username_key; Type: CONSTRAINT; Schema: public; Owner: xlsztrkmmnwlxj
--

ALTER TABLE ONLY public.usertable
    ADD CONSTRAINT "User_username_key" UNIQUE (username);


--
-- Name: client client_pkey; Type: CONSTRAINT; Schema: public; Owner: xlsztrkmmnwlxj
--

ALTER TABLE ONLY public.client
    ADD CONSTRAINT client_pkey PRIMARY KEY (userid);


--
-- Name: clientreservation clientreservation_owneruserid_parkingspotnumber_timeofstart_key; Type: CONSTRAINT; Schema: public; Owner: xlsztrkmmnwlxj
--

ALTER TABLE ONLY public.clientreservation
    ADD CONSTRAINT clientreservation_owneruserid_parkingspotnumber_timeofstart_key UNIQUE (owneruserid, parkingspotnumber, timeofstart);


--
-- Name: clientreservation clientreservation_pkey; Type: CONSTRAINT; Schema: public; Owner: xlsztrkmmnwlxj
--

ALTER TABLE ONLY public.clientreservation
    ADD CONSTRAINT clientreservation_pkey PRIMARY KEY (clientuserid, timeofstart);


--
-- Name: parking parking_pkey; Type: CONSTRAINT; Schema: public; Owner: xlsztrkmmnwlxj
--

ALTER TABLE ONLY public.parking
    ADD CONSTRAINT parking_pkey PRIMARY KEY (userid);


--
-- Name: parkingowner parkingowner_pkey; Type: CONSTRAINT; Schema: public; Owner: xlsztrkmmnwlxj
--

ALTER TABLE ONLY public.parkingowner
    ADD CONSTRAINT parkingowner_pkey PRIMARY KEY (userid);


--
-- Name: parkingspot parkingspot_pkey; Type: CONSTRAINT; Schema: public; Owner: xlsztrkmmnwlxj
--

ALTER TABLE ONLY public.parkingspot
    ADD CONSTRAINT parkingspot_pkey PRIMARY KEY (userid, parkingspotnumber);


--
-- Name: parkingspotoccupancy parkingspotoccupancy_pkey; Type: CONSTRAINT; Schema: public; Owner: xlsztrkmmnwlxj
--

ALTER TABLE ONLY public.parkingspotoccupancy
    ADD CONSTRAINT parkingspotoccupancy_pkey PRIMARY KEY (userid, parkingspotnumber, datefrom);


--
-- Name: parkingspotoccupancy parkingspotoccupancy_userid_parkingspotnumber_dateto_key; Type: CONSTRAINT; Schema: public; Owner: xlsztrkmmnwlxj
--

ALTER TABLE ONLY public.parkingspotoccupancy
    ADD CONSTRAINT parkingspotoccupancy_userid_parkingspotnumber_dateto_key UNIQUE (userid, parkingspotnumber, dateto);


--
-- Name: usertable updateownerclient; Type: TRIGGER; Schema: public; Owner: xlsztrkmmnwlxj
--

CREATE TRIGGER updateownerclient AFTER INSERT ON public.usertable FOR EACH ROW EXECUTE FUNCTION public.update_owner_client();


--
-- Name: client client_userid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: xlsztrkmmnwlxj
--

ALTER TABLE ONLY public.client
    ADD CONSTRAINT client_userid_fkey FOREIGN KEY (userid) REFERENCES public.usertable(userid);


--
-- Name: clientreservation clientreservation_clientuserid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: xlsztrkmmnwlxj
--

ALTER TABLE ONLY public.clientreservation
    ADD CONSTRAINT clientreservation_clientuserid_fkey FOREIGN KEY (clientuserid) REFERENCES public.client(userid);


--
-- Name: clientreservation clientreservation_owneruserid_parkingspotnumber_fkey; Type: FK CONSTRAINT; Schema: public; Owner: xlsztrkmmnwlxj
--

ALTER TABLE ONLY public.clientreservation
    ADD CONSTRAINT clientreservation_owneruserid_parkingspotnumber_fkey FOREIGN KEY (owneruserid, parkingspotnumber) REFERENCES public.parkingspot(userid, parkingspotnumber);


--
-- Name: parking parking_userid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: xlsztrkmmnwlxj
--

ALTER TABLE ONLY public.parking
    ADD CONSTRAINT parking_userid_fkey FOREIGN KEY (userid) REFERENCES public.parkingowner(userid) ON DELETE CASCADE;


--
-- Name: parkingowner parkingowner_userid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: xlsztrkmmnwlxj
--

ALTER TABLE ONLY public.parkingowner
    ADD CONSTRAINT parkingowner_userid_fkey FOREIGN KEY (userid) REFERENCES public.usertable(userid) ON DELETE CASCADE;


--
-- Name: parkingspot parkingspot_userid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: xlsztrkmmnwlxj
--

ALTER TABLE ONLY public.parkingspot
    ADD CONSTRAINT parkingspot_userid_fkey FOREIGN KEY (userid) REFERENCES public.parking(userid) ON DELETE CASCADE;


--
-- Name: parkingspotoccupancy parkingspotoccupancy_userid_parkingspotnumber_fkey; Type: FK CONSTRAINT; Schema: public; Owner: xlsztrkmmnwlxj
--

ALTER TABLE ONLY public.parkingspotoccupancy
    ADD CONSTRAINT parkingspotoccupancy_userid_parkingspotnumber_fkey FOREIGN KEY (userid, parkingspotnumber) REFERENCES public.parkingspot(userid, parkingspotnumber) ON DELETE CASCADE;


--
-- Name: LANGUAGE plpgsql; Type: ACL; Schema: -; Owner: postgres
--

GRANT ALL ON LANGUAGE plpgsql TO xlsztrkmmnwlxj;


--
-- PostgreSQL database dump complete
--

