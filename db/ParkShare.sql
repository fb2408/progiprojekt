--
-- PostgreSQL database dump
--

-- Dumped from database version 13.2
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

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: User; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."User" (
    userid integer NOT NULL,
    username character varying(35),
    userfirstname character varying(35),
    usersurname character varying(35),
    useremail character varying(50),
    temppassword character varying(35),
    usertype character varying(15),
    confirmed boolean NOT NULL
);


ALTER TABLE public."User" OWNER TO postgres;

--
-- Name: client; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.client (
    userid integer NOT NULL,
    walletbalance numeric NOT NULL
);


ALTER TABLE public.client OWNER TO postgres;

--
-- Name: clientreservation; Type: TABLE; Schema: public; Owner: postgres
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


ALTER TABLE public.clientreservation OWNER TO postgres;

--
-- Name: parking; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.parking (
    userid integer NOT NULL,
    parkingname character varying(50) NOT NULL,
    parkingphoto character varying(50),
    hourlyprice numeric,
    description character varying(1000)
);


ALTER TABLE public.parking OWNER TO postgres;

--
-- Name: parkingowner; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.parkingowner (
    userid integer NOT NULL,
    iban character(21) NOT NULL,
    idpicture character varying(50) NOT NULL
);


ALTER TABLE public.parkingowner OWNER TO postgres;

--
-- Name: parkingspot; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.parkingspot (
    userid integer NOT NULL,
    parkingspotnumber integer NOT NULL,
    parkingspottype character varying(20) NOT NULL,
    canbereserved boolean NOT NULL,
    description character varying(1000),
    point1 point NOT NULL,
    point2 point NOT NULL,
    point3 point NOT NULL,
    point4 point NOT NULL,
    CONSTRAINT parkingspot_parkingspottype_check CHECK ((upper((parkingspottype)::text) = ANY (ARRAY['BIKE'::text, 'CAR'::text])))
);


ALTER TABLE public.parkingspot OWNER TO postgres;

--
-- Name: parkingspotoccupancy; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.parkingspotoccupancy (
    userid integer NOT NULL,
    parkingspotnumber integer NOT NULL,
    datefrom timestamp without time zone NOT NULL,
    dateto timestamp without time zone,
    occupancy boolean NOT NULL
);


ALTER TABLE public.parkingspotoccupancy OWNER TO postgres;

--
-- Data for Name: User; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."User" (userid, username, userfirstname, usersurname, useremail, temppassword, usertype, confirmed) FROM stdin;
\.


--
-- Data for Name: client; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.client (userid, walletbalance) FROM stdin;
\.


--
-- Data for Name: clientreservation; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.clientreservation (clientuserid, owneruserid, parkingspotnumber, timeofstart, duration, recurring) FROM stdin;
\.


--
-- Data for Name: parking; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.parking (userid, parkingname, parkingphoto, hourlyprice, description) FROM stdin;
\.


--
-- Data for Name: parkingowner; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.parkingowner (userid, iban, idpicture) FROM stdin;
\.


--
-- Data for Name: parkingspot; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.parkingspot (userid, parkingspotnumber, parkingspottype, canbereserved, description, point1, point2, point3, point4) FROM stdin;
\.


--
-- Data for Name: parkingspotoccupancy; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.parkingspotoccupancy (userid, parkingspotnumber, datefrom, dateto, occupancy) FROM stdin;
\.


--
-- Name: User User_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."User"
    ADD CONSTRAINT "User_pkey" PRIMARY KEY (userid);


--
-- Name: User User_username_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."User"
    ADD CONSTRAINT "User_username_key" UNIQUE (username);


--
-- Name: client client_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.client
    ADD CONSTRAINT client_pkey PRIMARY KEY (userid);


--
-- Name: clientreservation clientreservation_owneruserid_parkingspotnumber_timeofstart_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.clientreservation
    ADD CONSTRAINT clientreservation_owneruserid_parkingspotnumber_timeofstart_key UNIQUE (owneruserid, parkingspotnumber, timeofstart);


--
-- Name: clientreservation clientreservation_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.clientreservation
    ADD CONSTRAINT clientreservation_pkey PRIMARY KEY (clientuserid, timeofstart);


--
-- Name: parking parking_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.parking
    ADD CONSTRAINT parking_pkey PRIMARY KEY (userid);


--
-- Name: parkingowner parkingowner_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.parkingowner
    ADD CONSTRAINT parkingowner_pkey PRIMARY KEY (userid);


--
-- Name: parkingspot parkingspot_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.parkingspot
    ADD CONSTRAINT parkingspot_pkey PRIMARY KEY (userid, parkingspotnumber);


--
-- Name: parkingspotoccupancy parkingspotoccupancy_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.parkingspotoccupancy
    ADD CONSTRAINT parkingspotoccupancy_pkey PRIMARY KEY (userid, parkingspotnumber, datefrom);


--
-- Name: parkingspotoccupancy parkingspotoccupancy_userid_parkingspotnumber_dateto_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.parkingspotoccupancy
    ADD CONSTRAINT parkingspotoccupancy_userid_parkingspotnumber_dateto_key UNIQUE (userid, parkingspotnumber, dateto);


--
-- Name: client client_userid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.client
    ADD CONSTRAINT client_userid_fkey FOREIGN KEY (userid) REFERENCES public."User"(userid);


--
-- Name: clientreservation clientreservation_clientuserid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.clientreservation
    ADD CONSTRAINT clientreservation_clientuserid_fkey FOREIGN KEY (clientuserid) REFERENCES public.client(userid);


--
-- Name: clientreservation clientreservation_owneruserid_parkingspotnumber_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.clientreservation
    ADD CONSTRAINT clientreservation_owneruserid_parkingspotnumber_fkey FOREIGN KEY (owneruserid, parkingspotnumber) REFERENCES public.parkingspot(userid, parkingspotnumber);


--
-- Name: parking parking_userid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.parking
    ADD CONSTRAINT parking_userid_fkey FOREIGN KEY (userid) REFERENCES public.parkingowner(userid) ON DELETE CASCADE;


--
-- Name: parkingowner parkingowner_userid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.parkingowner
    ADD CONSTRAINT parkingowner_userid_fkey FOREIGN KEY (userid) REFERENCES public."User"(userid) ON DELETE CASCADE;


--
-- Name: parkingspot parkingspot_userid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.parkingspot
    ADD CONSTRAINT parkingspot_userid_fkey FOREIGN KEY (userid) REFERENCES public.parking(userid) ON DELETE CASCADE;


--
-- Name: parkingspotoccupancy parkingspotoccupancy_userid_parkingspotnumber_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.parkingspotoccupancy
    ADD CONSTRAINT parkingspotoccupancy_userid_parkingspotnumber_fkey FOREIGN KEY (userid, parkingspotnumber) REFERENCES public.parkingspot(userid, parkingspotnumber) ON DELETE CASCADE;


--
-- PostgreSQL database dump complete
--

