package workshop.tools;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import com.kanishka.virustotal.dto.DomainReport;
import com.kanishka.virustotal.dto.FileScanReport;
import com.kanishka.virustotal.dto.IPAddressReport;
import com.kanishka.virustotal.dto.Resolution;
import com.kanishka.virustotal.dto.Sample;
import com.kanishka.virustotal.dto.ScanInfo;
import com.kanishka.virustotal.dto.URL;
import com.kanishka.virustotal.dto.VirusScanInfo;
import com.kanishka.virustotal.exception.APIKeyNotFoundException;
import com.kanishka.virustotal.exception.UnauthorizedAccessException;
import com.kanishka.virustotalv2.VirusTotalConfig;
import com.kanishka.virustotalv2.VirustotalPublicV2;
import com.kanishka.virustotalv2.VirustotalPublicV2Impl;

/***
 * <b>This malware analyzer uses the VirusTotal API:</b>
 * <p><i>https://www.virustotal.com/</i>
 * <p>
 * <p><b>Code written by:</b>
 * <p>Kanishka Dilshan (kdkanishka)
 * <p><b>And obtained from:</b>
 * <p><i>https://github.com/kdkanishka/Virustotal-Public-API-V2.0-Client</i>
 * 		
 * <p><b>1) Google API Key -</b> "ABQIAAAAjU2rmwAtT1V5ywWwRjtMBBTedjNnG5K0Dey2aytYdRlzAQsvKQ"
 * <p><b>2) VirusTotal API Key -</b> "8c89590ee6c610039ebc7d5bf924e6f00e5dd40ea866a6ce16ef9df84ac6d69d"
 *
 */
public class VirustotalAnalyzer {
	private static final String apiKey = "8c89590ee6c610039ebc7d5bf924e6f00e5dd40ea866a6ce16ef9df84ac6d69d";

	
	/**
	 * Scan a given file.
	 * 
	 * @param filepath - Path to file.
	 */
	public static void scanFile(String filepath) {
		try {
			VirusTotalConfig.getConfigInstance().setVirusTotalAPIKey(apiKey);
			VirustotalPublicV2 virusTotalRef = new VirustotalPublicV2Impl();

			ScanInfo scanInformation = virusTotalRef.scanFile(new File(filepath));

			System.out.println("___SCAN INFORMATION___");
			System.out.println("MD5 :\t" + scanInformation.getMd5());
			System.out.println("Perma Link :\t" + scanInformation.getPermalink());
			System.out.println("Resource :\t" + scanInformation.getResource());
			System.out.println("Scan Date :\t" + scanInformation.getScan_date());
			System.out.println("Scan Id :\t" + scanInformation.getScan_id());
			System.out.println("SHA1 :\t" + scanInformation.getSha1());
			System.out.println("SHA256 :\t" + scanInformation.getSha256());
			System.out.println("Verbose Msg :\t" + scanInformation.getVerbose_msg());
			System.out.println("Response Code :\t" + scanInformation.getResponse_code());
			System.out.println("done.");
		} catch (APIKeyNotFoundException ex) {
			System.err.println("API Key not found! " + ex.getMessage());
		} catch (UnsupportedEncodingException ex) {
			System.err.println("Unsupported Encoding Format!" + ex.getMessage());
		} catch (UnauthorizedAccessException ex) {
			System.err.println("Invalid API Key " + ex.getMessage());
		} catch (Exception ex) {
			System.err.println("Something Bad Happened! " + ex.getMessage());
		}
	}
	

	/***
	 * Get file scan report
	 * 
	 * @param filePath - Path to file.
	 */
	public static void getFileScanReport(String filePath) {
		try {
			VirusTotalConfig.getConfigInstance().setVirusTotalAPIKey(apiKey);
			VirustotalPublicV2 virusTotalRef = new VirustotalPublicV2Impl();

			FileScanReport report = virusTotalRef.getScanReport(filePath);

			System.out.println("MD5 :\t" + report.getMd5());
			System.out.println("Perma link :\t" + report.getPermalink());
			System.out.println("Resourve :\t" + report.getResource());
			System.out.println("Scan Date :\t" + report.getScan_date());
			System.out.println("Scan Id :\t" + report.getScan_id());
			System.out.println("SHA1 :\t" + report.getSha1());
			System.out.println("SHA256 :\t" + report.getSha256());
			System.out.println("Verbose Msg :\t" + report.getVerbose_msg());
			System.out.println("Response Code :\t" + report.getResponse_code());
			System.out.println("Positives :\t" + report.getPositives());
			System.out.println("Total :\t" + report.getTotal());

			HashMap<String, VirusScanInfo> scans = report.getScans();
			for (String key : scans.keySet()) {
				VirusScanInfo virusInfo = scans.get(key);
				System.out.println("Scanner : " + key);
				System.out.println("\t\t Resut : " + virusInfo.getResult());
				System.out.println("\t\t Update : " + virusInfo.getUpdate());
				System.out.println("\t\t Version :" + virusInfo.getVersion());
			}

		} catch (APIKeyNotFoundException ex) {
			System.err.println("API Key not found! " + ex.getMessage());
		} catch (UnsupportedEncodingException ex) {
			System.err.println("Unsupported Encoding Format!" + ex.getMessage());
		} catch (UnauthorizedAccessException ex) {
			System.err.println("Invalid API Key " + ex.getMessage());
		} catch (Exception ex) {
			System.err.println("Something Bad Happened! " + ex.getMessage());
		}
	}
	

	/**
	 * Scan a URL.
	 * 
	 * @param url - The URL to scan.
	 */
	public static void scanUrl(String url) {
		try {
			VirusTotalConfig.getConfigInstance().setVirusTotalAPIKey(apiKey);
			VirustotalPublicV2 virusTotalRef = new VirustotalPublicV2Impl();

			String urls[] = {url};
			ScanInfo[] scanInfoArr = virusTotalRef.scanUrls(urls);

			for (ScanInfo scanInformation : scanInfoArr) {
				System.out.println("___SCAN INFORMATION___");
				System.out.println("MD5 :\t" + scanInformation.getMd5());
				System.out.println("Perma Link :\t" + scanInformation.getPermalink());
				System.out.println("Resource :\t" + scanInformation.getResource());
				System.out.println("Scan Date :\t" + scanInformation.getScan_date());
				System.out.println("Scan Id :\t" + scanInformation.getScan_id());
				System.out.println("SHA1 :\t" + scanInformation.getSha1());
				System.out.println("SHA256 :\t" + scanInformation.getSha256());
				System.out.println("Verbose Msg :\t" + scanInformation.getVerbose_msg());
				System.out.println("Response Code :\t" + scanInformation.getResponse_code());
				System.out.println("done.");
			}

		} catch (APIKeyNotFoundException ex) {
			System.err.println("API Key not found! " + ex.getMessage());
		} catch (UnsupportedEncodingException ex) {
			System.err.println("Unsupported Encoding Format!" + ex.getMessage());
		} catch (UnauthorizedAccessException ex) {
			System.err.println("Invalid API Key " + ex.getMessage());
		} catch (Exception ex) {
			System.err.println("Something Bad Happened! " + ex.getMessage());
		}
	}
	
	
	/**
	 * Get URL Report
	 * 
	 * @param url - The URL.
	 * @return The report.
	 */
	public static String getUrlReport(String url){
		String result = "";
		try {
			VirusTotalConfig.getConfigInstance().setVirusTotalAPIKey(apiKey);
			VirustotalPublicV2 virusTotalRef = new VirustotalPublicV2Impl();

			String urls[] = {url};
			FileScanReport[] reports = virusTotalRef.getUrlScanReport(urls, false);

			for (FileScanReport report : reports) {
				if(report.getResponse_code()==0){
					continue;
				}
				result+= "MD5 :\t" + report.getMd5()+"\n";
				result+= "Perma link :\t" + report.getPermalink()+"\n";
				result+= "Resourve :\t" + report.getResource()+"\n";
				result+= "Scan Date :\t" + report.getScan_date()+"\n";
				result+= "Scan Id :\t" + report.getScan_id()+"\n";
				result+= "SHA1 :\t" + report.getSha1()+"\n";
				result+= "SHA256 :\t" + report.getSha256()+"\n";
				result+= "Verbose Msg :\t" + report.getVerbose_msg()+"\n";
				result+= "Response Code :\t" + report.getResponse_code()+"\n";
				result+= "Positives :\t" + report.getPositives()+"\n";
				result+= "Total :\t" + report.getTotal()+"\n";

			}

		} catch (Exception ex) {
			return "Failed To Generate Report: " + ex.getMessage()+"\n";
		}
		
		return result;
		
	}

	
	/**
	 * Get IP Address Report.
	 * 
	 * @param ip - The IP.
	 */
	public static void getIPReport(String ip) {
        try {
            VirusTotalConfig.getConfigInstance().setVirusTotalAPIKey(apiKey);
            VirustotalPublicV2 virusTotalRef = new VirustotalPublicV2Impl();

            IPAddressReport report = virusTotalRef.getIPAddresReport(ip);

            System.out.println("___IP Rport__");

            Sample[] communicatingSamples = report.getDetected_communicating_samples();
            if (communicatingSamples != null) {
                System.out.println("Communicating Samples");
                for (Sample sample : communicatingSamples) {
                    System.out.println("SHA256 : " + sample.getSha256());
                    System.out.println("Date : " + sample.getDate());
                    System.out.println("Positives : " + sample.getPositives());
                    System.out.println("Total : " + sample.getTotal());
                }
            }

            Sample[] detectedDownloadedSamples = report.getDetected_downloaded_samples();
            if (detectedDownloadedSamples != null) {
                System.out.println("Detected Downloaded Samples");
                for (Sample sample : detectedDownloadedSamples) {
                    System.out.println("SHA256 : " + sample.getSha256());
                    System.out.println("Date : " + sample.getDate());
                    System.out.println("Positives : " + sample.getPositives());
                    System.out.println("Total : " + sample.getTotal());
                }
            }

            URL[] urls = report.getDetected_urls();
            if (urls != null) {
                System.out.println("Detected URLs");
                for (URL url : urls) {
                    System.out.println("URL : " + url.getUrl());
                    System.out.println("Positives : " + url.getPositives());
                    System.out.println("Total : " + url.getTotal());
                    System.out.println("Scan Date" + url.getScan_date());
                }
            }

            Resolution[] resolutions = report.getResolutions();
            if (resolutions != null) {
                System.out.println("Resolutions");
                for (Resolution resolution : resolutions) {
                    System.out.println("IP Address : " + resolution.getIp_address());
                    System.out.println("Last Resolved : " + resolution.getLast_resolved());
                }
            }

            Sample[] unDetectedDownloadedSamples = report.getUndetected_downloaded_samples();
            if (unDetectedDownloadedSamples != null) {
                System.out.println("Undetected Downloaded Samples");
                for (Sample sample : unDetectedDownloadedSamples) {
                    System.out.println("SHA256 : " + sample.getSha256());
                    System.out.println("Date : " + sample.getDate());
                    System.out.println("Positives : " + sample.getPositives());
                    System.out.println("Total : " + sample.getTotal());
                }
            }

            Sample[] unDetectedCommunicatingSamples = report.getUndetected_communicating_samples();
            if (unDetectedCommunicatingSamples != null) {
                System.out.println("Undetected Communicating Samples");
                for (Sample sample : unDetectedCommunicatingSamples) {
                    System.out.println("SHA256 : " + sample.getSha256());
                    System.out.println("Date : " + sample.getDate());
                    System.out.println("Positives : " + sample.getPositives());
                    System.out.println("Total : " + sample.getTotal());
                }
            }

            System.out.println("Response Code : " + report.getResponse_code());
            System.out.println("Verbose Message : " + report.getVerbose_msg());



        } catch (APIKeyNotFoundException ex) {
            System.err.println("API Key not found! " + ex.getMessage());
        } catch (UnsupportedEncodingException ex) {
            System.err.println("Unsupported Encoding Format!" + ex.getMessage());
        } catch (UnauthorizedAccessException ex) {
            System.err.println("Invalid API Key " + ex.getMessage());
        } catch (Exception ex) {
            System.err.println("Something Bad Happened! " + ex.getMessage());
        }
    }
	
	
	/**
	 * Get Domain Report.
	 * 
	 * @param domain -The domain.
	 */
	public static void getDomainReport(String domain) {
        try {
            VirusTotalConfig.getConfigInstance().setVirusTotalAPIKey(apiKey);
            VirustotalPublicV2 virusTotalRef = new VirustotalPublicV2Impl();

            DomainReport report = virusTotalRef.getDomainReport(domain);
            System.out.println("___Domain Rport__");

            Sample[] communicatingSamples = report.getDetected_communicating_samples();
            if (communicatingSamples != null) {
                System.out.println("Communicating Samples");
                for (Sample sample : communicatingSamples) {
                    System.out.println("SHA256 : " + sample.getSha256());
                    System.out.println("Date : " + sample.getDate());
                    System.out.println("Positives : " + sample.getPositives());
                    System.out.println("Total : " + sample.getTotal());
                }
            }

            Sample[] detectedDownloadedSamples = report.getDetected_downloaded_samples();
            if (detectedDownloadedSamples != null) {
                System.out.println("Detected Downloaded Samples");
                for (Sample sample : detectedDownloadedSamples) {
                    System.out.println("SHA256 : " + sample.getSha256());
                    System.out.println("Date : " + sample.getDate());
                    System.out.println("Positives : " + sample.getPositives());
                    System.out.println("Total : " + sample.getTotal());
                }
            }

            URL[] urls = report.getDetected_urls();
            if (urls != null) {
                System.out.println("Detected URLs");
                for (URL url : urls) {
                    System.out.println("URL : " + url.getUrl());
                    System.out.println("Positives : " + url.getPositives());
                    System.out.println("Total : " + url.getTotal());
                    System.out.println("Scan Date" + url.getScan_date());
                }
            }

            Resolution[] resolutions = report.getResolutions();
            if (resolutions != null) {
                System.out.println("Resolutions");
                for (Resolution resolution : resolutions) {
                    System.out.println("IP Address : " + resolution.getIp_address());
                    System.out.println("Last Resolved : " + resolution.getLast_resolved());
                }
            }

            Sample[] unDetectedDownloadedSamples = report.getUndetected_downloaded_samples();
            if (unDetectedDownloadedSamples != null) {
                System.out.println("Undetected Downloaded Samples");
                for (Sample sample : unDetectedDownloadedSamples) {
                    System.out.println("SHA256 : " + sample.getSha256());
                    System.out.println("Date : " + sample.getDate());
                    System.out.println("Positives : " + sample.getPositives());
                    System.out.println("Total : " + sample.getTotal());
                }
            }

            Sample[] unDetectedCommunicatingSamples = report.getUndetected_communicating_samples();
            if (unDetectedCommunicatingSamples != null) {
                System.out.println("Undetected Communicating Samples");
                for (Sample sample : unDetectedCommunicatingSamples) {
                    System.out.println("SHA256 : " + sample.getSha256());
                    System.out.println("Date : " + sample.getDate());
                    System.out.println("Positives : " + sample.getPositives());
                    System.out.println("Total : " + sample.getTotal());
                }
            }
            
            System.out.println("Response Code : " + report.getResponse_code());
            System.out.println("Verbose Message : " + report.getVerbose_msg());



        } catch (APIKeyNotFoundException ex) {
            System.err.println("API Key not found! " + ex.getMessage());
        } catch (UnsupportedEncodingException ex) {
            System.err.println("Unsupported Encoding Format!" + ex.getMessage());
        } catch (UnauthorizedAccessException ex) {
            System.err.println("Invalid API Key " + ex.getMessage());
        } catch (Exception ex) {
            System.err.println("Something Bad Happened! " + ex.getMessage());
        }
    }
	
}
