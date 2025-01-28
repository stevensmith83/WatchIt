import React, { useState } from 'react';
import axios from 'axios';
import { Container, Form, Button, Row, Col, Card } from 'react-bootstrap';

interface MovieResult {
  id: number;
  name: string;
  image_url: string;
}

interface SourceEntity {
  logo_url: string;
  region: string;
}

const App: React.FC = () => {
  const [searchQuery, setSearchQuery] = useState('');
  const [region, setRegion] = useState('');
  const [results, setResults] = useState<MovieResult[]>([]);
  const [sources, setSources] = useState<SourceEntity[]>([]);
  const [selectedMovie, setSelectedMovie] = useState<MovieResult | null>(null);
  const [isLoading, setIsLoading] = useState(false);

  const handleSearch = async () => {
    if (!searchQuery.trim()) {
      alert('Please enter a search term.');
      return;
    }

    setIsLoading(true);
    try {
      const response = await axios.get(`http://localhost:8080/api/movies/search`, {
        params: {
          title: searchQuery
        }
      });

      setResults(response.data.results);
      setSelectedMovie(null); // Reset selected movie when searching for new results
      setSources([]); // Reset sources when searching for new results
    } catch (error) {
      console.error('Error fetching data:', error);
      alert('Failed to fetch results. Please try again later.');
    } finally {
      setIsLoading(false);
    }
  };

  const handleMovieClick = async (movie: MovieResult, customRegion?: string) => {
    setIsLoading(true);
    setSelectedMovie(movie);
    setSources([]); // Reset sources when searching for new results

    try {
	  const params: Record<string, string | number | undefined> = {
        id: movie.id,
      };

      // Only include region if customRegion or state region is not empty
      if (customRegion || region) {
        params.region = customRegion || region;
      }
	
	  const response = await axios.get(`http://localhost:8080/api/movies/sources`, { params });

      setSources(response.data);
    } catch (error) {
      console.error('Error fetching sources:', error);
      alert('Failed to fetch sources. Please try again later.');
    } finally {
      setIsLoading(false);
    }
  };

  const handleRegionChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    const selectedRegion = e.target.value;
    setRegion(selectedRegion);

    // If a movie is selected, update its sources based on the new region
    if (selectedMovie) {
      handleMovieClick(selectedMovie, selectedRegion);
    }
  };

  const handleKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
	if (e.key === 'Enter') {
	  e.preventDefault(); // Prevent form submission
	  handleSearch(); // Trigger search when Enter is pressed
    }
  };

  return (
    <Container className="mt-4">
      <h1 className="text-center mb-4">WatchIt!</h1>
      <Form>
        <Row className="mb-3">
          <Col sm={8}>
            <Form.Control
              type="text"
              placeholder="Search for a movie or actor"
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              onKeyDown={handleKeyDown} // Add keydown listener
            />
          </Col>
          <Col sm={4}>
            <Form.Select value={region} onChange={handleRegionChange}>
              <option value="">All region</option>
              <option value="US">USA</option>
              <option value="CA">Canada</option>
              <option value="GB">England</option>
            </Form.Select>
          </Col>
        </Row>
        <div className="text-center">
          <Button variant="primary" onClick={handleSearch} disabled={isLoading}>
            {isLoading ? 'Searching...' : 'Search'}
          </Button>
        </div>
      </Form>

      {/* Selected Movie Row */}
      {selectedMovie && (
        <div className="mt-3">
          <h2>Selected Movie:</h2>
          <Row className="mb-3">
            <Col md={12}>
              <Card className="mb-3">
                <Card.Body className="d-flex">
                  <img
                    src={selectedMovie.image_url}
                    alt={selectedMovie.name}
                    style={{ width: '120px', height: '180px', marginRight: '20px' }}
                  />
                  <div>
                    <Card.Title>{selectedMovie.name}</Card.Title>
                    {sources.length > 0 && (
                      <div>
                        <p>Available Sources:</p>
                        {sources.map((source, index) => (
                          <img
                            key={index}
                            src={source.logo_url}
                            alt={source.region}
                            style={{ marginRight: '20px' }}
                          />
                        ))}
                      </div>
                    )}
                  </div>
                </Card.Body>
              </Card>
            </Col>
          </Row>
        </div>
      )}

      {/* Movie Grid */}
      <div className="mt-3">
        <h2>Results:</h2>
        {results.length === 0 && !isLoading && <p>No results found.</p>}
        <Row>
          {results
            .filter((result) => !selectedMovie || result.id !== selectedMovie.id) // Exclude the selected movie from the grid
            .map((result) => (
              <Col md={3} key={result.id} className="mb-3">
                <Card
                  onClick={() => handleMovieClick(result)}
                  style={{ cursor: 'pointer' }}
                >
                  <Card.Img variant="top" src={result.image_url} alt={result.name} />
                  <Card.Body>
                    <Card.Title>{result.name}</Card.Title>
                  </Card.Body>
                </Card>
              </Col>
            ))}
        </Row>
      </div>
    </Container>
  );
};

export default App;
